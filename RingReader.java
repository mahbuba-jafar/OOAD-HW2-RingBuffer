public class RingReader {
    private RingBuffer buffer;
    private long nextSeq;

    public RingReader(RingBuffer buffer, long startSeq) {
    this.buffer = buffer;
    this.nextSeq = startSeq;
}
}
