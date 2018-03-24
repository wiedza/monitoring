/* WIEDZA
 * -----------------
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 */
package org.wiedza.monitoring.api.services.threads;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        this.onError = onError;
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
            try {
                Future<T> itemFuture = completion.poll(timeLeft, TimeUnit.MILLISECONDS);
                if (itemFuture != null) {
                    T taskData = itemFuture.get();
                    data.add(taskData);
                    tasksLeft = tasksLeft - 1;
                }
            } catch (ExecutionException | InterruptedException error) {
            }
        }

        List<Runnable> tasksNotRunnings = executor.shutdownNow();
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

}
