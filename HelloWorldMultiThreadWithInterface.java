public class HelloWorldMultiThreadWithInterface {
    //preferred way
    public static class Greeter implements Runnable{
        private String country;

        public Greeter(String country){
            this.country = country;
        }

        public void run(){
            try {
                Thread.sleep(100);
            }catch(InterruptedException e){
//ignored for now
            }
            System.out.println("Hello " + country+"!");
        }
    }
    public static void main(String[] args){
        String[] countries = {"India", "Japan", "Germany", "USA"};
        for(String country: countries){
            //Thread also implements Runnable and if a Runnable is passed in the constructor it uses this instances' run method
            Greeter greeter = new Greeter(country);
            new Thread(greeter, country+" thread").start();
        }
    }
}
