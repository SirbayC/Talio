package client.utils;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class RepeatingFutureTest {

    @Test
    void testIsCancelled() {
        RepeatingFuture<Integer> future = new RepeatingFuture<>();
        assertFalse(future.isCancelled());
        future.cancel();
        assertTrue(future.isCancelled());
    }

    @Test
    void testComplete() {
        RepeatingFuture<String> future = new RepeatingFuture<>();
        CountDownLatch countDownLatch = new CountDownLatch(2);

        Consumer<String> consumer = value -> {
            assertEquals("test", value);
            countDownLatch.countDown();
        };

        future.thenAccept(consumer);
        future.complete("test");
        future.complete("test");
        assertEquals(0, countDownLatch.getCount());
    }

    @Test
    void testCompleteCancelledFuture() {
        RepeatingFuture<String> future = new RepeatingFuture<>();
        future.cancel();
        assertThrows(IllegalStateException.class, () -> future.complete("test"));
    }
}
