package creatingThreads.customExceptionHandling;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class Main {
    public static class ThreadExceptionHandler implements Thread.UncaughtExceptionHandler {
        private ExecutionException exception;

        public ExecutionException getException() {
            return exception;
        }

        //creates a custom uncaughtException handler which stores the thrown e
        // as a object of ExecutionException(e) in exception property
        public void uncaughtException(Thread t, Throwable e) {
            exception = new ExecutionException(e);
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        Kenny kenny = new Kenny();
        System.out.println("Starting Kenny");

        Thread t = new Thread(kenny, "Kenny");

        ThreadExceptionHandler exceptionHandler = new ThreadExceptionHandler();
        t.setUncaughtExceptionHandler(exceptionHandler);

        t.start();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            //Can ignore
        }
        t.interrupt();
        try {
            t.join();
        } catch (InterruptedException e) {
            //Ignore
        }
    }

}
