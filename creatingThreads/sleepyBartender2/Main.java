package creatingThreads.sleepyBartender2;

import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args){
        Bartender bartender = new Bartender();
        Thread bartenderThread = new Thread(bartender, "Bartender");
        int numCustomers = 5;
        Thread[] customerThreads = new Thread[numCustomers];

        bartenderThread.start();
        try{
            TimeUnit.SECONDS.sleep(1);
        }
        catch(InterruptedException e){
            //this can be ignored
        }

        for(int i=1; i<=numCustomers ; i++){
            String customerName = "Customer "+i;
            Customer customer = new Customer(bartenderThread, customerName, (int)(Math.random() * 10));
            customerThreads[i-1] = new Thread(customer, customerName);
            customerThreads[i-1].start();
        }
        //polling for isAlive is waste of CPU time
        //If thread sleep is used instead - depending on time if short or long,
        // it could have to check more times or thread could become unresponsive resp.
        //Instead join is used
        try{
            bartenderThread.join();
            //join checks isAlive and when thread dies, sends a signal by notifying on the thread object
            //the thread that calls join(here Main thread) waits for that signal

            //Note: never call wait on a object that is a thread...
        }catch (InterruptedException e){
            //can be ignored
        }
        System.out.println("A voice: Hey! Isn't that the bartender sneaking out of the door?");
        for(int i=1; i<=numCustomers ; i++){
            //What if bartender.interrupt() is called in the customer thread before this interrupt,
            // after bartender goes away? The bartender interrupt will be called and customerThread exits
            //interrupt does nothing on a terminated thread
            customerThreads[i-1].interrupt();
        }

    }
}
