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

    public RingReader createReader(){

    long startSeq = Math.max(0, writSeq - capacity);
    return new RingReader(this, startSeq);
    }

    public void write(int value){
   
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
        int index = (int) (seq % capacity);
        return storage[index];
    }
    

}
