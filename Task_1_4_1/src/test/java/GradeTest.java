import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    public void getKnownGrade() {
        Grade grade = new Grade(Assessment.EXAM, 1, 5);
        assertEquals(5, grade.getGrade());
    }

    @Test
    public void getUnknownGrade() {
        Grade grade = new Grade(Assessment.EXAM, 1);
        assertThrows(IllegalStateException.class, grade::getGrade);
    }

    @Test
    public void checkKnownGrade() {
        Grade grade = new Grade(Assessment.EXAM, 1, 5);
        assertTrue(grade.isKnownGrade());
    }

    @Test
    public void checkUnknownGrade() {
        Grade grade = new Grade(Assessment.EXAM, 1);
        assertFalse(grade.isKnownGrade());
    }
}