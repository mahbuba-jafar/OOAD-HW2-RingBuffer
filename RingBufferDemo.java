public class RingBufferDemo {
    public static void main(String[] args) {
        RingBuffer buffer = new RingBuffer(3);

        RingReader slowReader = buffer.createReader();

        buffer.write(1);
        buffer.write(2);
        buffer.write(3);
        buffer.write(4);
        buffer.write(5);

        ReadResult result = slowReader.read();
        System.out.println("Slow reader first read:");
        System.out.println(result);
    }
}