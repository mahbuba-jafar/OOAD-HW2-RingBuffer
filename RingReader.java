public class RingReader {
    private RingBuffer buffer;
    private long nextSeq;

    public RingReader(RingBuffer buffer, long startSeq) {
    this.buffer = buffer;
    this.nextSeq = startSeq;
}

ReadResult read(){
    // if(nextSeq < )
    long missed = 0;
    long oldest = buffer.getOldestSeq();
    if(nextSeq < oldest){
        missed = oldest - nextSeq;
        nextSeq = oldest;
    }

    long write = buffer.getWriteSeq();
    if(nextSeq >= write){
        return new ReadResult(false, 0, missed);
    }
    int value = buffer.getValueAt(nextSeq);
    nextSeq++;
    return new ReadResult(true, value, missed);
}
}
