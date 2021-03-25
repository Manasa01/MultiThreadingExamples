package sharingMemory.producerConsumer;

import java.util.ArrayList;
import java.util.List;

public class Producer implements Runnable{
    private volatile List<ProduceObserver> observers = new ArrayList<>();
    public void registerObserver(ProduceObserver observer){
        observers.add(observer);
    }
    public void run(){
        System.out.println("Producer starting");
        for(int i=1; i<=10 ; i++){
            try{
                Thread.sleep(1000);
            }catch (InterruptedException e){
                //Can ignore this
            }
            //Builder pattern to create Produce - makes the instance immutable
            Produce.ProduceBuilder builder = new Produce.ProduceBuilder();
            builder.withInstance(i);
            builder.withColor(Produce.Color.values()[i % Produce.Color.values().length]);
            Produce latestProduce = builder.build();
            //publish to other threads
            observers.forEach(observer -> observer.onProduction(latestProduce));
        }
        System.out.println("Producer terminating.");
    }
}
