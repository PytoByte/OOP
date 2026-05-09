package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import org.junit.jupiter.api.Test;

class CheckResultTest {
    private static final CheckAssignment MOCK_CHECK_ASSIGNMENT = new CheckAssignment(
            new Group("group", Collections.emptyMap()),
            new Student("student", "nick"),
            new Task("id", "title", 0, Collections.emptyList())
    );

    @Test
    void failedDownload() {
        CheckResult checkResult = CheckResult.failedDownload(MOCK_CHECK_ASSIGNMENT);
        assertFalse(checkResult.download());
        assertEquals(MOCK_CHECK_ASSIGNMENT, checkResult.checkAssignment());
    }

    @Test
    void taskNotFound() {
        CheckResult checkResult = CheckResult.taskNotFound(MOCK_CHECK_ASSIGNMENT);
        assertTrue(checkResult.download());
        assertFalse(checkResult.taskFound());
        assertEquals(MOCK_CHECK_ASSIGNMENT, checkResult.checkAssignment());
    }
}