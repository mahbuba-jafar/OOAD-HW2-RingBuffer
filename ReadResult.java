public final class ReadResult {

    private final boolean hasValue;
    private final int value;
    private final long missedCount;

    private ReadResult(boolean hasValue, int value, long missedCount) {
        this.hasValue = hasValue;
        this.value = value;
        this.missedCount = missedCount;
    }

    public static ReadResult empty(long missedCount) {
        return new ReadResult(false, 0, missedCount);
    }

    public static ReadResult of(int value, long missedCount) {
        return new ReadResult(true, value, missedCount);
    }

    public boolean hasValue() {
        return hasValue;
    }

    public int value() {
        if (!hasValue) {
            throw new IllegalStateException("No value available. Call hasValue() first.");
        }
        return value;
    }

    public long missedCount() {
        return missedCount;
    }

    @Override
    public String toString() {
        if (!hasValue) {
            return "ReadResult{hasValue=false, missedCount=" + missedCount + "}";
        }
        return "ReadResult{hasValue=true, value=" + value + ", missedCount=" + missedCount + "}";
    }
}