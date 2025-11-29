import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Test for grade class.
 */
class GradeTest {
    @Test
    public void init() {
        assertDoesNotThrow(() -> new Grade(Assessment.EXAM, 3, 5));
    }

    @Test
    public void initUnknown() {
        assertDoesNotThrow(() -> new Grade(Assessment.EXAM, 3));
    }

    @Test
    public void initUnknownSemesterLowNumber() {
        assertThrows(IllegalArgumentException.class, () -> new Grade(Assessment.EXAM, 0));
    }

    @Test
    public void initUnknownSemesterHighNumber() {
        assertThrows(IllegalArgumentException.class, () -> new Grade(Assessment.EXAM, 9));
    }

    @Test
    public void initBadSemesterLowNumber() {
        assertThrows(IllegalArgumentException.class, () -> new Grade(Assessment.EXAM, 0, 5));
    }

    @Test
    public void initBadSemesterHighNumber() {
        assertThrows(IllegalArgumentException.class, () -> new Grade(Assessment.EXAM, 9, 5));
    }

    @Test
    public void initBadGradeLowNumber() {
        assertThrows(IllegalArgumentException.class, () -> new Grade(Assessment.EXAM, 1, 0));
    }

    @Test
    public void initBadGradeHighNumber() {
        assertThrows(IllegalArgumentException.class, () -> new Grade(Assessment.EXAM, 1, 6));
    }

    @Test
    public void initBadGradeCreditLowNumber() {
        assertThrows(IllegalArgumentException.class, () -> new Grade(Assessment.CREDIT, 1, -1));
    }

    @Test
    public void initBadGradeCreditHighNumber() {
        assertThrows(IllegalArgumentException.class, () -> new Grade(Assessment.CREDIT, 1, 2));
    }
}