package model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TestResultsTest {
    @Test
    void testErrorFactoryMethod() {
        TestResults errorResults = TestResults.error();
        assertTrue(errorResults.isError());
    }
}