public class RingBufferDemo {

    public static void main(String[] args) {

        // ------------------------------------------------------------
        // Scenario 1: Empty buffer read
        // Requirement: read should not crash; should report "no value"
        // ------------------------------------------------------------
        System.out.println("=== Scenario 1: Empty read ===");
        RingBuffer buffer1 = new RingBuffer(3);
        RingReader rEmpty = buffer1.createReader();

        ReadResult empty = rEmpty.read();
        System.out.println("Empty read result: " + empty);
        // Expected: hasValue=false, missedCount=0


        // ------------------------------------------------------------
        // Scenario 2: Multiple readers read independently
        // Requirements:
        // - multiple readers
        // - each has its own reading position
        // - reading by one reader does NOT remove for the other
        // ------------------------------------------------------------
        System.out.println("\n=== Scenario 2: Independent readers ===");
        RingBuffer buffer2 = new RingBuffer(3);

        RingReader r1 = buffer2.createReader();
        RingReader r2 = buffer2.createReader();

        buffer2.write(1);
        buffer2.write(2);
        buffer2.write(3);

        // r1 reads first two items
        System.out.println("r1 read #1: " + r1.read()); // expects value=1
        System.out.println("r1 read #2: " + r1.read()); // expects value=2

        // r2 should still be able to read from the beginning (independent position)
        System.out.println("r2 read #1: " + r2.read()); // expects value=1
        System.out.println("r2 read #2: " + r2.read()); // expects value=2
        System.out.println("r2 read #3: " + r2.read()); // expects value=3

        // r1 should still be able to read remaining item
        System.out.println("r1 read #3: " + r1.read()); // expects value=3

        // now both should be empty
        System.out.println("r1 after end: " + r1.read()); // expects hasValue=false
        System.out.println("r2 after end: " + r2.read()); // expects hasValue=false


        // ------------------------------------------------------------
        // Scenario 3: Overwrite & missed items
        // Requirements:
        // - buffer overwrites oldest when full
        // - slow readers may miss items (missedCount > 0)
        // ------------------------------------------------------------
        System.out.println("\n=== Scenario 3: Overwrite (slow reader misses) ===");
        RingBuffer buffer3 = new RingBuffer(3);

        // Create reader BEFORE writing, so it starts at seq=0
        RingReader slow = buffer3.createReader();

        // Write more than capacity (3), so oldest gets overwritten
        buffer3.write(1);
        buffer3.write(2);
        buffer3.write(3);
        buffer3.write(4);
        buffer3.write(5);

        // Now buffer holds the most recent 3 items: 3,4,5
        // slow reader was at seq=0 and should miss 2 items (1 and 2)
        ReadResult firstSlowRead = slow.read();
        System.out.println("slow first read: " + firstSlowRead);
        // Expected: hasValue=true, value=3, missedCount=2

        System.out.println("slow second read: " + slow.read()); // expects value=4, missedCount=0
        System.out.println("slow third read: " + slow.read());  // expects value=5, missedCount=0
        System.out.println("slow after end: " + slow.read());   // expects hasValue=false, missedCount=0


        System.out.println("\n=== Done ===");
    }
}