public class RingReader {
    private RingBuffer buffer;
    private long nextSeq;

    public RingReader(RingBuffer buffer, long startSeq) {
    this.buffer = buffer;
    this.nextSeq = startSeq;
}

int read(){
    // if(nextSeq < )
    long oldest = buffer.getOldestSeq();
    if(nextSeq < oldest){
        nextSeq = oldest;
    }

    long write = buffer.getWriteSeq();
    if(nextSeq >= write){
        return -1;
    }
    int value = buffer.getValueAt(nextSeq);
    nextSeq++;
    return value;
}
}
