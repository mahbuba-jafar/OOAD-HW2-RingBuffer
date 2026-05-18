# RingBuffer Test Suite

Comprehensive unit tests for the RingBuffer circular buffer implementation.

## Test Files

### RingBufferTest.java
Tests for the `RingBuffer` class:
- Constructor initialization
- Single and multiple writes
- Write wrap-around behavior
- Sequence tracking (getWriteSeq, getOldestSeq)
- Value retrieval at specific sequences
- Reader creation
- Edge cases: small capacity (1), large capacity (1000), multiple overflows

**11 test cases**

### RingReaderTest.java
Tests for the `RingReader` class:
- Reading from empty buffer
- Sequential reads
- Multiple readers on same buffer
- Reader falling behind and missing messages
- Reader creation at different times
- Reader behavior after buffer overflow
- Value reading and missed count tracking

**10 test cases**

### ReadResultTest.java
Tests for the `ReadResult` immutable result object:
- Empty result factory and getters
- Result with value factory
- Value retrieval with exception on empty
- Missed count tracking
- toString() representations
- Edge cases: negative values, zero values, large counts

**16 test cases**

### IntegrationTest.java
End-to-end tests combining all components:
- Producer-consumer pattern
- Overflow with missed messages
- Multiple concurrent readers
- Readers created at different times
- Rapid read/write cycles
- Complex mixed operations
- Sequence boundary conditions

**11 test cases**

### TestRunner.java
Master test runner that executes all test suites.

## Running Tests

### Individual Tests
```bash
javac test/*.java
java -cp . test.RingBufferTest
java -cp . test.RingReaderTest
java -cp . test.ReadResultTest
java -cp . test.IntegrationTest
```

### All Tests
```bash
javac test/*.java
java -cp . test.TestRunner
```

## Test Coverage

**Total: 48 test cases**

- **RingBuffer**: Capacity handling, write operations, sequence tracking, initialization
- **RingReader**: Read operations, missed message detection, reader independence
- **ReadResult**: Result states, value access, exception handling
- **Integration**: Producer-consumer, overflow, multi-reader scenarios

## Edge Cases Tested

✓ Empty buffer reads
✓ Buffer overflow and wraparound
✓ Multiple readers from same buffer
✓ Reader falling behind
✓ Capacity boundaries (1, 5, 100, 1000)
✓ Large write sequences
✓ Missed message detection
✓ Exception on accessing value from empty result
✓ Negative and zero values
✓ Mixed rapid operations

## No External Dependencies

Tests use only Java built-in features. No JUnit or external libraries required.
