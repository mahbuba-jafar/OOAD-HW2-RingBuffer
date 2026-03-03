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
   
    long getWriteSeq(){
        return writSeq;
    }

    long getOldestSeq(){
        return Math.max(0, writSeq - capacity);
    }

    int getValueAt(long seq){
        int index = (int) seq % capacity;
        return storage[index];
    }
    public static void main(String[] args) {
        RingBuffer buffer = new RingBuffer(3);

        RingReader r1 = buffer.createReader();
        RingReader r2 = buffer.createReader();

        buffer.write(1);
        buffer.write(2);
        buffer.write(3);

        System.out.println("r1: " + r1.read());
        System.out.println("r1: " + r1.read());
        System.out.println("r1: " + r1.read());

        System.out.println("r2: " + r2.read());
        System.out.println("r2: " + r2.read() + "\n");

        buffer.write(4);

        System.out.println("r1: " + r1.read());
        System.out.println("r1: " + r1.read());
        System.out.println("r2: " + r2.read());
        // System.out.println("r2: " + r2.read());

    }
}
