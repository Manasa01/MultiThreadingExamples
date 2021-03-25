package creatingThreads.customExceptionHandling;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class Kenny implements Runnable {
    public void run() {
        System.out.println("Hello team!");

        Thread currentThread = Thread.currentThread();

        while (!currentThread.isInterrupted()) {
            System.out.println("I'm happy working!");
            try {
                TimeUnit.MILLISECONDS.sleep(500);

            } catch (InterruptedException e) {
                currentThread.interrupt();
            }
        }
        throw new RuntimeException("Goodbye! I'm resigning!");
    }
}
