public class ReadResult {
    private boolean hasValue;
    private int value;
    private long missedCount;

    public ReadResult(boolean hasValue, int value, long missedCount){

        this.hasValue = hasValue;
        this.value = value;
        this.missedCount = missedCount;
    }

    public boolean hasValue(){
        return hasValue;
    }

    public int value(){
        return value;
    }

     public long missedCount(){
        return missedCount;
    }

    @Override
public String toString() {
    return "ReadResult{hasValue=" + hasValue +
            ", value=" + value +
            ", missedCount=" + missedCount + "}";
}
}
