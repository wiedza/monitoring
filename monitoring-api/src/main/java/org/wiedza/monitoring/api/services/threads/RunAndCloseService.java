/* WIEDZA
 * -----------------
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 */
package org.wiedza.monitoring.api.services.threads;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

import org.wiedza.monitoring.api.exceptions.Asserts;
import org.wiedza.monitoring.api.loggers.Loggers;
import org.wiedza.monitoring.api.time.Chrono;

/**
 * ThreadsExecutor
 * 
 * @author patrickguillerm
 * @since 24 mars 2018
 */
public class RunAndCloseService<T> implements ThreadFactory {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final String                                threadsName;

    private final List<Callable<T>>                     tasks;

    private final Map<Future<T>, Callable<T>>           tasksAndFutures;

    private final long                                  timeout;

    private final BiFunction<Exception, Callable<T>, T> onError;

    private final ExecutorService                       executor;

    private final CompletionService<T>                  completion;

    private final ThreadGroup                           threadGroup;

    private final AtomicInteger                         threadIndex = new AtomicInteger();

    private final List<T>                               data        = new ArrayList<>();

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    @SafeVarargs
    public RunAndCloseService(final String threadsName, final long timeout, final int nbThreads,
            final BiFunction<Exception, Callable<T>, T> onError, final Callable<T>... tasks) {
        this(threadsName, timeout, nbThreads, Arrays.asList(tasks), onError);
    }

    @SafeVarargs
    public RunAndCloseService(final String threadsName, final long timeout, final int nbThreads,
            final Callable<T>... tasks) {
        this(threadsName, timeout, nbThreads, Arrays.asList(tasks), null);
    }

    public RunAndCloseService(final String threadsName, final long timeout, final int nbThreads,
            final List<Callable<T>> tasks) {
        this(threadsName, timeout, nbThreads, tasks, null);
    }

    public RunAndCloseService(final String threadsName, final long timeout, final int nbThreads,
            final List<Callable<T>> tasks, final BiFunction<Exception, Callable<T>, T> onError) {
        super();
        Asserts.isNotNull(tasks);

        this.tasks = tasks;
        int howManyThreads = tasks.size() < nbThreads ? tasks.size() : nbThreads;
        this.threadsName = threadsName;
        this.timeout = timeout;
        this.onError = onError == null ? this::handlerError : onError;
        tasksAndFutures = new HashMap<>();
        threadGroup = Thread.currentThread().getThreadGroup();
        executor = Executors.newFixedThreadPool(howManyThreads, this);
        completion = new ExecutorCompletionService<T>(executor);
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    public List<T> run() {
        final List<Future<T>> futures = sumitTask();
        int tasksLeft = futures.size();
        long timeLeft = timeout;
        final Chrono chrono = Chrono.startChrono();

        while (tasksLeft > 0 && chrono.snapshot().getDuration() < timeout) {
            timeLeft = computeTimeLeft(timeLeft, chrono);
            Future<T> itemFuture = null;
            T taskData = null;
            try {
                itemFuture = completion.poll(timeLeft, TimeUnit.MILLISECONDS);
                if (itemFuture != null) {
                    taskData = itemFuture.get();
                    tasksLeft = tasksLeft - 1;
                }
            } catch (ExecutionException | InterruptedException error) {
                Callable<T> task = resolveTask(itemFuture);
                taskData = handlerError(error, task);
                tasksLeft = tasksLeft - 1;
            }

            if (taskData != null) {
                data.add(taskData);
            }
        }
        executor.shutdown();

        data.addAll(handlerTimeoutTask());
        return data;
    }

    private long computeTimeLeft(long timeLeft, final Chrono chrono) {
        long result = timeLeft - chrono.snapshot().getDuration();
        return result < 0 ? 0 : result;
    }

    // =========================================================================
    // PRIVATE
    // =========================================================================
    private List<Future<T>> sumitTask() {
        final List<Future<T>> result = new ArrayList<>();
        for (Callable<T> task : tasks) {
            final Future<T> future = completion.submit(task);
            result.add(future);
            tasksAndFutures.put(future, task);
        }
        return result;
    }

    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public Thread newThread(Runnable runnable) {
        final String name = String.join(".", threadsName, String.valueOf(threadIndex.getAndIncrement()));
        final Thread result = new Thread(threadGroup, runnable, name, 0);
        result.setDaemon(false);
        return result;
    }

    // =========================================================================
    // ERRORS
    // =========================================================================
    private T handlerError(Exception error, Callable<T> task) {
        Loggers.DEBUGLOG.error(error.getMessage(), error);
        return null;
    }

    private List<T> handlerTimeoutTask() {
        List<T> result = new ArrayList<>();
        for (Map.Entry<Future<T>, Callable<T>> entry : tasksAndFutures.entrySet()) {
            if (!entry.getKey().isDone()) {
                T taskData = onError.apply(new TimeoutException(), entry.getValue());
                if (taskData != null) {
                    result.add(taskData);
                }
            }
        }
        return result;
    }

    private Callable<T> resolveTask(Future<T> itemFuture) {
        // TODO Auto-generated method stub
        return null;
    }

}
