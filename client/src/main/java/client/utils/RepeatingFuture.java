package client.utils;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class RepeatingFuture<V> {

    private final AtomicBoolean cancelled = new AtomicBoolean(false);
    private final List<Consumer<V>> consumers = new CopyOnWriteArrayList<>();

    /**
     * Check if future was cancelled
     *
     * @return whether it is cancelled
     */
    public boolean isCancelled() {
        return cancelled.get();
    }

    /**
     * Cancel future
     */
    public void cancel() {
        cancelled.set(true);
    }

    /**
     * Complete future
     *
     * @param value value
     */
    public void complete(V value) {
        if(isCancelled())
            throw new IllegalStateException("cannot complete cancelled future");

        consumers.forEach(c -> c.accept(value));
    }

    /**
     * Accept value
     *
     * @param c consumer
     */
    public void thenAccept(Consumer<V> c) {
        consumers.add(c);
    }

}
