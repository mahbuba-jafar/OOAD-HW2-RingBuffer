public class RingReaderTest {
    private static int passed = 0, failed = 0;

    public static void main(String[] args) {
        test("read from empty buffer", () -> {
            RingBuffer buf = new RingBuffer(5);
            RingReader reader = buf.createReader();
            ReadResult res = reader.read();
            assert !res.hasValue();
            assert res.missedCount() == 0;
        });

        test("read single value", () -> {
            RingBuffer buf = new RingBuffer(5);
            buf.write(42);
            RingReader reader = buf.createReader();
            ReadResult res = reader.read();
            assert res.hasValue();
            assert res.value() == 42;
            assert res.missedCount() == 0;
        });

        test("read multiple values", () -> {
            RingBuffer buf = new RingBuffer(5);
            buf.write(10);
            buf.write(20);
            buf.write(30);
            RingReader reader = buf.createReader();

            ReadResult r1 = reader.read();
            assert r1.hasValue() && r1.value() == 10;

            ReadResult r2 = reader.read();
            assert r2.hasValue() && r2.value() == 20;

            ReadResult r3 = reader.read();
            assert r3.hasValue() && r3.value() == 30;
        });

        test("read all then empty", () -> {
            RingBuffer buf = new RingBuffer(5);
            buf.write(1);
            buf.write(2);
            RingReader reader = buf.createReader();

            ReadResult r1 = reader.read();
            assert r1.hasValue();
            ReadResult r2 = reader.read();
            assert r2.hasValue();
            ReadResult r3 = reader.read();
            assert !r3.hasValue();
        });

        test("reader falls behind", () -> {
            RingBuffer buf = new RingBuffer(3);
            buf.write(1);
            buf.write(2);
            buf.write(3);

            RingReader reader = buf.createReader();
            ReadResult r1 = reader.read();
            assert r1.hasValue() && r1.value() == 1;

            buf.write(4);
            buf.write(5);
            buf.write(6);
            buf.write(7);

            ReadResult r2 = reader.read();
            assert r2.hasValue();
            assert r2.missedCount() == 1;
        });

        test("reader created mid-write", () -> {
            RingBuffer buf = new RingBuffer(5);
            buf.write(1);
            buf.write(2);
            RingReader reader = buf.createReader();
            buf.write(3);

            ReadResult r1 = reader.read();
            assert r1.hasValue() && r1.value() == 1;
            ReadResult r2 = reader.read();
            assert r2.hasValue() && r2.value() == 2;
            ReadResult r3 = reader.read();
            assert r3.hasValue() && r3.value() == 3;
        });

        test("reader after overflow", () -> {
            RingBuffer buf = new RingBuffer(3);
            for (int i = 0; i < 10; i++) buf.write(i);

            RingReader reader = buf.createReader();
            ReadResult r1 = reader.read();
            assert r1.hasValue() && r1.value() == 7;
            assert r1.missedCount() == 0;
        });

        test("multiple readers independent", () -> {
            RingBuffer buf = new RingBuffer(5);
            buf.write(1);
            buf.write(2);

            RingReader reader1 = buf.createReader();
            buf.write(3);
            RingReader reader2 = buf.createReader();
            buf.write(4);

            ReadResult r1a = reader1.read();
            assert r1a.hasValue() && r1a.value() == 1;

            ReadResult r2a = reader2.read();
            assert r2a.hasValue() && r2a.value() == 3;
        });

        test("reader skips missed data", () -> {
            RingBuffer buf = new RingBuffer(2);
            buf.write(1);
            buf.write(2);
            buf.write(3);
            buf.write(4);
            buf.write(5);

            RingReader reader = buf.createReader();
            ReadResult r = reader.read();
            assert r.missedCount() > 0;
            assert r.hasValue();
        });

        test("read after value() exception", () -> {
            RingBuffer buf = new RingBuffer(5);
            RingReader reader = buf.createReader();
            ReadResult res = reader.read();

            try {
                res.value();
                assert false : "Should throw";
            } catch (IllegalStateException e) {
                // expected
            }
            assert !res.hasValue();
        });

        printResults("RingReaderTest");
    }

    private static void test(String name, TestCase testCase) {
        try {
            testCase.run();
            System.out.println("✓ " + name);
            passed++;
        } catch (Exception e) {
            System.out.println("✗ " + name + ": " + e.getMessage());
            e.printStackTrace();
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
