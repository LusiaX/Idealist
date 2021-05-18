package idealist.util;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Charles Cui<lfylccxm@hotmail.com> on 2021-05-19 00:17:02
 */
public abstract class ThreadUtils {
    private final static ExecutorService threads = Executors.newCachedThreadPool();

    public static void execute(Runnable command) {
        threads.execute(command);
    }

    public static <T> Future<T> submit(Callable<T> task) {
        return threads.submit(task);
    }

    public static <T> Future<T> submit(Runnable task, T result) {
        return threads.submit(task, result);
    }

    public static Future<?> submit(Runnable task) {
        return threads.submit(task);
    }

    public static <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) {
        try {
            return threads.invokeAll(tasks);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T invokeAny(Collection<? extends Callable<T>> tasks) {
        try {
            return threads.invokeAny(tasks);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T invokeAny(Collection<? extends Callable<T>> tasks,
                                  long timeout, TimeUnit unit) {
        try {
            return threads.invokeAny(tasks, timeout, unit);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
