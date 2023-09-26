package client.utils;

import javafx.concurrent.Task;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Supplier;

public class TaskWrapper {

    /**
     * Wrap async interaction with possible UI update in future
     * <p>
     * Whenever we make a blocking call (http request, reading a file etc.) after the user interacts with the UI
     * We should be running that in a separate thread as to not block updates and further interaction with the UI
     * The issue then becomes that we want to update the UI which is not allowed in a different thread
     * JavaFX uses Task<V> for this to allow for an async operation that returns to the UI Thread
     * This method serves as an easier interaction with it using CompletableFuture, so we don't have to repeat all of it
     *
     * @param supplier the blocking call to execute async
     * @param <T>      type of the calls return value
     * @return future
     */
    public static <T> CompletableFuture<T> wrap(Supplier<T> supplier) {
        Task<T> task = new Task<>() {
            protected T call() {
                return supplier.get();
            }
        };

        CompletableFuture<T> future = new CompletableFuture<>();
        task.setOnSucceeded(state -> {
            try {
                T obj = task.get();
                future.complete(obj);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });
        task.setOnFailed(state -> future.completeExceptionally(task.getException()));

        ForkJoinPool.commonPool().execute(task);
        return future;
    }

}
