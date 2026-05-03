package Domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestResultsTest {
    @Test
    void testErrorFactoryMethod() {
        TestResults errorResults = TestResults.error();
        assertTrue(errorResults.isError());
    }
}