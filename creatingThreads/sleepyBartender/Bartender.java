package creatingThreads.sleepyBartender;

import java.util.concurrent.TimeUnit;

public class Bartender implements Runnable {
    public void run() {
        System.out.println("Bartender: My boss isn't in today. Time for a quick nap!");
        while (true) {
            if (Thread.interrupted()) {
                // interrupted() method can be applied only on the current thread,
                // since it checks if an interrupt has occured and also clears it
                // Unlike isInterrupted which is an instance method, and only checks if a thread has been interrupted
                // If isInterrupted was used, interrupt should be cleared, else the sleep code will not execute
                // and bartender will be awake always
                System.out.println("Bartender: Zzz, is someone waiting?");
            }
            //if there is an interrupt after line 9 and before sleep? it will be handled right away due to InterruptedException
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                // InterruptedException catch clears the interrupt,
                // Hence in order to serve the customer, we will create an interrupt ourselves ,
                // so that if condn in line 9 is executed
                Thread.currentThread().interrupt();
            }
        }
    }
}
