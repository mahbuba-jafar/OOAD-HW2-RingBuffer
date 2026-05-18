public class IntegrationTest {
    private static int passed = 0, failed = 0;

    public static void main(String[] args) {
        test("producer-consumer basic", () -> {
            RingBuffer buf = new RingBuffer(5);
            for (int i = 0; i < 5; i++) buf.write(i);

            RingReader reader = buf.createReader();
            for (int i = 0; i < 5; i++) {
                ReadResult res = reader.read();
                assert res.hasValue() && res.value() == i;
            }
            ReadResult empty = reader.read();
            assert !empty.hasValue();
        });

        test("producer-consumer with overflow", () -> {
            RingBuffer buf = new RingBuffer(3);
            for (int i = 0; i < 3; i++) buf.write(i);

            RingReader reader = buf.createReader();
            ReadResult r1 = reader.read();
            assert r1.hasValue() && r1.value() == 0;

            buf.write(3);
            buf.write(4);
            buf.write(5);

            ReadResult r2 = reader.read();
            assert r2.hasValue() && r2.missedCount() == 1;
        });

        test("multiple concurrent readers", () -> {
            RingBuffer buf = new RingBuffer(5);
            RingReader reader1 = buf.createReader();
            buf.write(1);
            buf.write(2);

            RingReader reader2 = buf.createReader();
            buf.write(3);

            ReadResult r1a = reader1.read();
            ReadResult r1b = reader1.read();
            ReadResult r1c = reader1.read();

            assert r1a.value() == 1;
            assert r1b.value() == 2;
            assert r1c.value() == 3;

            ReadResult r2a = reader2.read();
            ReadResult r2b = reader2.read();

            assert r2a.value() == 2;
            assert r2b.value() == 3;
        });

        test("reader creates at different times", () -> {
            RingBuffer buf = new RingBuffer(10);
            for (int i = 0; i < 5; i++) buf.write(i);

            RingReader earlyReader = buf.createReader();
            for (int i = 5; i < 15; i++) buf.write(i);
            RingReader lateReader = buf.createReader();

            ReadResult early1 = earlyReader.read();
            assert early1.hasValue() && early1.value() == 0;

            ReadResult late1 = lateReader.read();
            assert late1.hasValue() && late1.value() == 5;
            assert late1.missedCount() == 0;
        });

        test("reader at capacity boundary", () -> {
            RingBuffer buf = new RingBuffer(4);
            for (int i = 0; i < 4; i++) buf.write(i);

            RingReader reader = buf.createReader();
            for (int i = 0; i < 4; i++) {
                ReadResult res = reader.read();
                assert res.hasValue() && res.value() == i;
                assert res.missedCount() == 0;
            }
        });

        test("rapid writes and reads", () -> {
            RingBuffer buf = new RingBuffer(100);
            for (int i = 0; i < 500; i++) buf.write(i);

            RingReader reader = buf.createReader();
            int readCount = 0;
            while (true) {
                ReadResult res = reader.read();
                if (!res.hasValue()) break;
                readCount++;
            }
            assert readCount == 100;
        });

        test("wrap around detection", () -> {
            RingBuffer buf = new RingBuffer(3);
            buf.write(1);
            buf.write(2);
            buf.write(3);

            RingReader reader = buf.createReader();
            reader.read();

            buf.write(4);
            buf.write(5);
            buf.write(6);

            ReadResult res = reader.read();
            assert res.missedCount() == 1;
        });

        test("read after complete overflow", () -> {
            RingBuffer buf = new RingBuffer(2);
            for (int i = 0; i < 20; i++) buf.write(i);

            RingReader reader = buf.createReader();
            ReadResult res = reader.read();
            assert res.hasValue();
            assert res.missedCount() >= 0;
        });

        test("mixed operations", () -> {
            RingBuffer buf = new RingBuffer(5);
            buf.write(10);
            buf.write(20);

            RingReader r1 = buf.createReader();
            ReadResult res1 = r1.read();
            assert res1.value() == 10;

            buf.write(30);
            buf.write(40);
            buf.write(50);
            buf.write(60);

            ReadResult res2 = r1.read();
            assert res2.hasValue();
            assert res2.missedCount() >= 0;

            RingReader r2 = buf.createReader();
            ReadResult res3 = r2.read();
            assert res3.hasValue();
        });

        test("sequence counting", () -> {
            RingBuffer buf = new RingBuffer(5);
            assert buf.getWriteSeq() == 0;
            assert buf.getOldestSeq() == 0;

            for (int i = 0; i < 10; i++) {
                buf.write(i);
            }

            assert buf.getWriteSeq() == 10;
            assert buf.getOldestSeq() == 5;
        });

        printResults("IntegrationTest");
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
