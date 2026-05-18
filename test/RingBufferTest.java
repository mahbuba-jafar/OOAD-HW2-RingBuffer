public class RingBufferTest {
    private static int passed = 0, failed = 0;

    public static void main(String[] args) {
        test("constructor", () -> {
            RingBuffer buf = new RingBuffer(10);
            assert buf.getWriteSeq() == 0;
            assert buf.getOldestSeq() == 0;
        });

        test("write single", () -> {
            RingBuffer buf = new RingBuffer(5);
            buf.write(42);
            assert buf.getWriteSeq() == 1;
            assert buf.getValueAt(0) == 42;
        });

        test("write multiple", () -> {
            RingBuffer buf = new RingBuffer(5);
            for (int i = 0; i < 5; i++) buf.write(i);
            assert buf.getWriteSeq() == 5;
            assert buf.getOldestSeq() == 0;
        });

        test("write wrap around", () -> {
            RingBuffer buf = new RingBuffer(5);
            for (int i = 0; i < 7; i++) buf.write(i);
            assert buf.getWriteSeq() == 7;
            assert buf.getOldestSeq() == 2;
            assert buf.getValueAt(2) == 2;
            assert buf.getValueAt(6) == 6;
        });

        test("get oldest seq", () -> {
            RingBuffer buf = new RingBuffer(5);
            assert buf.getOldestSeq() == 0;
            buf.write(1);
            assert buf.getOldestSeq() == 0;
            for (int i = 0; i < 5; i++) buf.write(i);
            buf.write(99);
            assert buf.getOldestSeq() == 1;
        });

        test("get value at", () -> {
            RingBuffer buf = new RingBuffer(5);
            buf.write(10);
            buf.write(20);
            buf.write(30);
            assert buf.getValueAt(0) == 10;
            assert buf.getValueAt(1) == 20;
            assert buf.getValueAt(2) == 30;
        });

        test("create reader", () -> {
            RingBuffer buf = new RingBuffer(5);
            buf.write(1);
            buf.write(2);
            RingReader reader = buf.createReader();
            assert reader != null;
        });

        test("create reader after overflow", () -> {
            RingBuffer buf = new RingBuffer(5);
            for (int i = 0; i < 10; i++) buf.write(i);
            RingReader reader = buf.createReader();
            assert reader != null;
        });

        test("small capacity", () -> {
            RingBuffer buf = new RingBuffer(1);
            buf.write(100);
            assert buf.getValueAt(0) == 100;
            buf.write(200);
            assert buf.getValueAt(0) == 200;
            assert buf.getOldestSeq() == 1;
        });

        test("large capacity", () -> {
            RingBuffer buf = new RingBuffer(1000);
            for (int i = 0; i < 1000; i++) buf.write(i);
            assert buf.getWriteSeq() == 1000;
            assert buf.getOldestSeq() == 0;
        });

        test("multiple overflows", () -> {
            RingBuffer buf = new RingBuffer(5);
            for (int i = 0; i < 20; i++) buf.write(i);
            assert buf.getWriteSeq() == 20;
            assert buf.getOldestSeq() == 15;
        });

        printResults("RingBufferTest");
    }

    private static void test(String name, TestCase testCase) {
        try {
            testCase.run();
            System.out.println("✓ " + name);
            passed++;
        } catch (Exception e) {
            System.out.println("✗ " + name + ": " + e.getMessage());
            failed++;
        }
    }

    private static void printResults(String className) {
        System.out.println("\n" + className + ": " + passed + " passed, " + failed + " failed");
    }

    interface TestCase {
        void run() throws Exception;
    }
}
