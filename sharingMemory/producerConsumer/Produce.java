package sharingMemory.producerConsumer;

public class Produce {
    public static class ProduceBuilder{
        //not made volatile since this is not shared
        private int instance = 0; //like a key
        private Color color;

        public void withInstance(int instance){
            this.instance = instance;
        }
        public void withColor(Color color){
            this.color = color;
        }
        public Produce build(){
            return new Produce(this.instance, this.color);
        }
    }
    enum Color {Red, Blue, Green, Yellow};
//other threads will always see the initial values after these fields have been initialised by the builder, and no setters too
    private final int instance;
    private final Color color;

    private Produce(int instance, Color color){
        this.instance = instance;
        this.color = color;
    }
    public int getInstance(){
        return this.instance;
    }

    public Color getColor(){
        return this.color;
    }
}
