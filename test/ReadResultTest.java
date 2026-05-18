public class ReadResultTest {
    private static int passed = 0, failed = 0;

    public static void main(String[] args) {
        test("empty result has no value", () -> {
            ReadResult res = ReadResult.empty(0);
            assert !res.hasValue();
        });

        test("empty result missed count", () -> {
            ReadResult res = ReadResult.empty(5);
            assert res.missedCount() == 5;
        });

        test("of result has value", () -> {
            ReadResult res = ReadResult.of(42, 0);
            assert res.hasValue();
            assert res.value() == 42;
        });

        test("of result with missed count", () -> {
            ReadResult res = ReadResult.of(100, 3);
            assert res.hasValue();
            assert res.value() == 100;
            assert res.missedCount() == 3;
        });

        test("value throws on empty", () -> {
            ReadResult res = ReadResult.empty(0);
            try {
                res.value();
                assert false : "Should throw";
            } catch (IllegalStateException e) {
                assert e.getMessage() != null;
            }
        });

        test("value returns correct int", () -> {
            ReadResult res = ReadResult.of(999, 0);
            assert res.value() == 999;
        });

        test("negative value", () -> {
            ReadResult res = ReadResult.of(-42, 0);
            assert res.value() == -42;
        });

        test("zero value", () -> {
            ReadResult res = ReadResult.of(0, 0);
            assert res.hasValue();
            assert res.value() == 0;
        });

        test("large missed count", () -> {
            ReadResult res = ReadResult.of(1, 1000000);
            assert res.missedCount() == 1000000;
        });

        test("toString empty", () -> {
            ReadResult res = ReadResult.empty(5);
            String str = res.toString();
            assert str.contains("false");
            assert str.contains("5");
        });

        test("toString with value", () -> {
            ReadResult res = ReadResult.of(42, 0);
            String str = res.toString();
            assert str.contains("true");
            assert str.contains("42");
        });

        test("empty with large missed count toString", () -> {
            ReadResult res = ReadResult.empty(999);
            String str = res.toString();
            assert str.contains("999");
            assert !str.contains("value=");
        });

        test("of with value and missed toString", () -> {
            ReadResult res = ReadResult.of(123, 456);
            String str = res.toString();
            assert str.contains("123");
            assert str.contains("456");
        });

        test("multiple empty results equal semantics", () -> {
            ReadResult r1 = ReadResult.empty(0);
            ReadResult r2 = ReadResult.empty(0);
            assert !r1.hasValue();
            assert !r2.hasValue();
            assert r1.missedCount() == r2.missedCount();
        });

        test("multiple of results", () -> {
            ReadResult r1 = ReadResult.of(10, 0);
            ReadResult r2 = ReadResult.of(20, 0);
            assert r1.value() != r2.value();
            assert r1.hasValue() && r2.hasValue();
        });

        printResults("ReadResultTest");
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
