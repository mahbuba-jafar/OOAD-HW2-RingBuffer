public class RingBuffer {
   //fixed capacity N
    private int capacity;
    private int storage[];
    private long writSeq;

    public RingBuffer(int capacity){
        this.capacity = capacity;
        writSeq = 0;
        storage = new int[capacity];


    }

    RingReader createReader(){

    long startSeq = Math.max(0, writSeq - capacity);
    return new RingReader(this, startSeq);
    }

    void write(int value){
   
        int index;
        index = (int)(writSeq % capacity);
        storage[index] = value;
        writSeq++;
    }
   

    public static void main(String[] args) {
        
    }
}
