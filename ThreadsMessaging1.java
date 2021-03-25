import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadsMessaging1 {

    private static CountDownLatch latch;
    public static final AtomicInteger waitCounter = new AtomicInteger(0); //goes from
    public static final AtomicInteger activeProcesses = new AtomicInteger(0);
    public static class Process implements Runnable{
        //Best practice to use Runnable interface rather than inheriting Thread
        private int processId;
        private int n;
        private ArrayList<Process> neighbors;
        private ArrayList<Thread> neighborThreads;
        private int randomId;
        private boolean neighborInMIS = false;
        private HashSet<Integer> MISSet;

        public Process(int id, int num, HashSet<Integer> mis){
            processId = id;
            n = num;
            neighbors = new ArrayList<Process>();
            neighborThreads = new ArrayList<Thread>();
            MISSet = mis;
        }
        public ArrayList<Process> getNeighbors(){
            return neighbors;
        }
        public void setNeighbor(Process neighbor){
            neighbors.add(neighbor);
        }
        public ArrayList<Thread> getNeighborThreads(){
            return neighborThreads;
        }
        public void setNeighbor(Thread neighborThread){
            neighborThreads.add(neighborThread);
        }
        public int getRandomId(){
            return randomId;
        }
        public void setRandomId(){
            randomId = (int)(Math.random() * Math.pow(n,4));
        }
        public int getProcessId(){
            return processId;
        }

        public void setNeighborInMIS(){
            //must be accessed by only one thread at a time?
            if(!neighborInMIS){
                System.out.println("Received Done message.");
                neighborInMIS = true;
            }
        }
        public void sendMessageToNeighbor(){
            for(int i=0; i<neighborThreads.size(); i++){

                if(neighborThreads.get(i).isAlive()){
                    System.out.println("Sending Done message to neighbour: "+ neighborThreads.get(i).getName());
                    neighbors.get(i).setNeighborInMIS();
                }
            }
        }
        public void waitForAllProcesses(){
            waitCounter.getAndIncrement();
            while(waitCounter.get() < activeProcesses.get());
            waitCounter.compareAndSet(activeProcesses.get(),0);
        }
        public void run(){
            /* // for testing only
            System.out.println("My Id is: "+processId);
            System.out.println("No of neighbors: "+neighbors.size());
            for(Process ngbr: neighbors) {
                System.out.println("My Neighbors' Name is: " + ngbr.getName());
                System.out.println("My Neighbors' Id is: " + ngbr.getId());
            }
            */
            //wait for all threads to start before executing
            waitCounter.getAndIncrement();
            while(waitCounter.get() < n);
            waitCounter.compareAndSet(n,0);

            System.out.println(processId + " Started executing...");
            while(true) {
                //TODO: Generate random ID and send to all threads, wait for all to finish
                //TODO: compare received random ID and send Done, wait for all to finish
                if (processId == 2) { //TODO: update to compare with max ID
                    //send "Done" message to others
                    MISSet.add(processId);
                    sendMessageToNeighbor();

                    waitForAllProcesses();

                    activeProcesses.getAndDecrement();
                    latch.countDown();
                    break;
                }
                waitForAllProcesses();

                //wait for all to send
                // all updates to neighborInMIS are done at this point
                //if received Done message
                if (neighborInMIS) {
                    //terminate
                    System.out.println("Process Id: " + processId + " is going to terminate.");
                    waitForAllProcesses();
                    activeProcesses.getAndDecrement();
                    latch.countDown();
                    break;
                }
                waitForAllProcesses();
            }
        }
    }

    public static void main(String[] args){
       //Master thread generates threads
        //TODO: Get n,uid,adj.matrix from input.txt
        int n = 3;
        activeProcesses.set(n);
        HashSet<Integer> MISSet = new HashSet<>();
        Thread[] threads = new Thread[n];
        Process[] processes = new Process[n];
        for(int i=0; i<n; i++) {
            processes[i] = new Process(i , n, MISSet);
            threads[i] = new Thread(processes[i], i+"_thread");
        }
        //TODO: assign neighbours using adj. matrix

        // here manually done 0-1-2
        processes[0].setNeighbor(processes[2]);
        processes[2].setNeighbor(processes[0]);
        processes[1].setNeighbor(processes[2]);
        processes[2].setNeighbor(processes[1]);
        processes[0].setNeighbor(threads[2]);
        processes[2].setNeighbor(threads[0]);
        processes[1].setNeighbor(threads[2]);
        processes[2].setNeighbor(threads[1]);

        latch = new CountDownLatch(n);

        //start threads
        for(int i=0; i<n; i++) //NOTE: threads should start running after all start
            threads[i].start();

        // The main thread waits for all the threads to terminate
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //OUTPUT
        //TODO: Output no. of rounds
        //Show processes in MIS
        System.out.println("Processes in MIS:");
        for(Integer id : MISSet){
            System.out.println(id);
        }

        System.exit(0); //does not exit otherwise, why?
        //how to terminate? how best to send Done message? how to update MIS? MIS set

        //TODO : Sequential algo to verify MIS
    }
}
