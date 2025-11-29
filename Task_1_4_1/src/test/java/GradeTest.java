import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for grade class.
 */
class GradeTest {
    @Test
    public void init() {
        Grade grade = new Grade(Assessment.EXAM, 3, 5);
    }

    @Test
    public void initBadSemesterLowNumber() {
        assertThrows(IllegalArgumentException.class, () -> new Grade(Assessment.EXAM, -10, 5));
    }

    @Test
    public void initBadSemesterHighNumber() {
        assertThrows(IllegalArgumentException.class, () -> new Grade(Assessment.EXAM, 10, 5));
    }

    @Test
    public void initBadGradeLowNumber() {
        assertThrows(IllegalArgumentException.class, () -> new Grade(Assessment.EXAM, 1, -10));
    }

    @Test
    public void initBadGradeHighNumber() {
        assertThrows(IllegalArgumentException.class, () -> new Grade(Assessment.EXAM, 1, 10));
    }

    @Test
    public void initBadGradeCreditLowNumber() {
        assertThrows(IllegalArgumentException.class, () -> new Grade(Assessment.CREDIT, 1, 2));
    }

    @Test
    public void initBadGradeCreditHighNumber() {
        assertThrows(IllegalArgumentException.class, () -> new Grade(Assessment.CREDIT, 1, -1));
    }
}