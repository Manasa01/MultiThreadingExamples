package creatingThreads.dateFormatterThreadLocal;

import java.text.SimpleDateFormat;

public class DateFormatterExample {
    public static class DateFormatterThreadLocal extends ThreadLocal<SimpleDateFormat>{
        //Note: Thread Local can create leaks - if it stays and thread dies - must take care of this Leak!
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("EEE MMM d, hh:mm:ss");
        }
    }
    public static DateFormatterThreadLocal dateFormatterVar = new DateFormatterThreadLocal();
    public static void main(String[] args){
        Thread t1 = new Thread(new DatePrinter("Formatter 1"), "Formatter 1");
        Thread t2 = new Thread(new DatePrinter("Formatter 2"), "Formatter 2");
        t1.start();
        t2.start();
    }
}
