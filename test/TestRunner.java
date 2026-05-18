public class TestRunner {
    public static void main(String[] args) {
        System.out.println("=== RingBuffer Test Suite ===\n");

        System.out.println("Running RingBufferTest...");
        RingBufferTest.main(args);

        System.out.println("\nRunning RingReaderTest...");
        RingReaderTest.main(args);

        System.out.println("\nRunning ReadResultTest...");
        ReadResultTest.main(args);

        System.out.println("\nRunning IntegrationTest...");
        IntegrationTest.main(args);

        System.out.println("\n=== All tests completed ===");
    }
}
