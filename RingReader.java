public class RingReader {
    private RingBuffer buffer;
    private long nextSeq;

    public RingReader(RingBuffer buffer, long startSeq) {
    this.buffer = buffer;
    this.nextSeq = startSeq;
}

public ReadResult read(){
    // if(nextSeq < )
    long missed = 0;
    long oldest = buffer.getOldestSeq();
    if(nextSeq < oldest){
        missed = oldest - nextSeq;
        nextSeq = oldest;
    }

    long write = buffer.getWriteSeq();

    if (nextSeq >= write) {
    return ReadResult.empty(missed);
    }
    int value = buffer.getValueAt(nextSeq);
    nextSeq++;
    return ReadResult.of(value, missed);
}
}