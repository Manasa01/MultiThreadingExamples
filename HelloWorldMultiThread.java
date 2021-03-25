import java.lang.Thread;

public class HelloWorldMultiThread {
    public static class Greeter extends Thread{
        private String country;

        public Greeter(String country){
            super(country +" thread");
            this.country = country;
        }
        //overrides the run method in Thread
        // - inheriting is not a good idea though: cannot inherit from others unless higher up in the hierarchy,
        // run may keep getting modified, scope of class, subclasses may keep changing...
        public void run(){
            // String id = Thread.currentThread().getName(); //param in above super() is seen
            long id = Thread.currentThread().getId();
            System.out.println("Hello " + country+"! from id: "+id);
        }
    }
    public static void main(String[] args){
        String[] countries = {"India", "Japan", "Germany", "USA"};
        for(String country: countries){
            new Greeter(country).start(); //this also showed id = 1???
            // greets.run(); //never use this as it uses the current thread's run method - so becomes single threaded
        }
    }

}
