package client.utils;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

public class TaskWrapperTest {

    @Test
    public void wrap_shouldReturnFutureWithValue(){
        String expected = "Hello, world!";
        Supplier<String> supplier = () -> expected;
        CompletableFuture<String> future = TaskWrapper.wrap(supplier);
        future.thenRun(() -> {
            assertEquals(expected, future.getNow("notHelloWorld"));
        });
    }

    @Test
    public void wrap_shouldReturnFutureWithException() {
        RuntimeException exception = new RuntimeException("Oops!");
        Supplier<String> supplier = () -> {
            throw exception;
        };
        CompletableFuture<String> future = TaskWrapper.wrap(supplier);
        future.thenRun(() -> assertThrows(ExecutionException.class, future::get));
        future.exceptionally(e -> {
            assertEquals(exception, e.getCause());
            return "";
        });
    }
}
