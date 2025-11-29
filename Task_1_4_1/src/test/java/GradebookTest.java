import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests for gradebook class.
 */
public class GradebookTest {

    @Test
    public void testGetCurrentAverageGrade_EmptyGrades() {
        Gradebook gradebook = new Gradebook();
        assertEquals(0.0, gradebook.getCurrentAverageGrade(), 0.001);
    }

    @Test
    public void testGetCurrentAverageGrade_WithCredits() {
        Gradebook gradebook = new Gradebook();
        gradebook.addGrade(new Grade(Assessment.EXAM, 1, 4));
        gradebook.addGrade(new Grade(Assessment.CREDIT, 1, 1));
        gradebook.addGrade(new Grade(Assessment.EXAM, 1, 5));

        assertEquals(4.5, gradebook.getCurrentAverageGrade(), 0.001);
    }

    @Test
    public void testGetCurrentAverageGrade_NoCredits() {
        Gradebook gradebook = new Gradebook();
        gradebook.addGrade(new Grade(Assessment.EXAM, 1, 4));
        gradebook.addGrade(new Grade(Assessment.DIFF_CREDIT, 1, 3));

        assertEquals(3.5, gradebook.getCurrentAverageGrade(), 0.001);
    }

    @Test
    public void testCanTransferToBudget_EmptyGrades() {
        Gradebook gradebook = new Gradebook();
        assertFalse(gradebook.canTransferToBudget());
    }

    @Test
    public void testCanTransferToBudget_OnFirstSemester() {
        Gradebook gradebook = new Gradebook();
        gradebook.addGrade(new Grade(Assessment.EXAM, 1, 5));

        assertFalse(gradebook.canTransferToBudget());
    }

    @Test
    public void testCanTransferToBudget_CurrentSemesterHasBadExam() {
        Gradebook gradebook = new Gradebook();
        gradebook.addGrade(new Grade(Assessment.EXAM, 1, 5));
        gradebook.addGrade(new Grade(Assessment.EXAM, 2, 3));

        assertFalse(gradebook.canTransferToBudget());
    }

    @Test
    public void testCanTransferToBudget_CurrentSemesterHasBadCredit() {
        Gradebook gradebook = new Gradebook();
        gradebook.addGrade(new Grade(Assessment.CREDIT, 1, 1));
        gradebook.addGrade(new Grade(Assessment.CREDIT, 2, 0));

        assertFalse(gradebook.canTransferToBudget());
    }

    @Test
    public void testCanTransferToBudget_CurrentSemesterHasBadDiffCredit() {
        Gradebook gradebook = new Gradebook();
        gradebook.addGrade(new Grade(Assessment.CREDIT, 1, 1));
        gradebook.addGrade(new Grade(Assessment.DIFF_CREDIT, 2, 2));

        assertFalse(gradebook.canTransferToBudget());
    }

    @Test
    public void testCanTransferToBudget_CurrentSemesterHasBadPracticeReport() {
        Gradebook gradebook = new Gradebook();
        gradebook.addGrade(new Grade(Assessment.CREDIT, 1, 1));
        gradebook.addGrade(new Grade(Assessment.PRAC_REP_PROT, 2, 2));

        assertFalse(gradebook.canTransferToBudget());
    }

    @Test
    public void testCanTransferToBudget_PreviousSemesterBadExam() {
        Gradebook gradebook = new Gradebook();
        gradebook.addGrade(new Grade(Assessment.EXAM, 1, 3));
        gradebook.addGrade(new Grade(Assessment.EXAM, 2, 5));

        assertFalse(gradebook.canTransferToBudget());
    }

    @Test
    public void testCanTransferToBudget_PreviousSemesterBadCredit() {
        Gradebook gradebook = new Gradebook();
        gradebook.addGrade(new Grade(Assessment.CREDIT, 1, 0));
        gradebook.addGrade(new Grade(Assessment.EXAM, 2, 5));

        assertFalse(gradebook.canTransferToBudget());
    }

    @Test
    public void testCanTransferToBudget_PreviousSemesterBadDiffCredit() {
        Gradebook gradebook = new Gradebook();
        gradebook.addGrade(new Grade(Assessment.DIFF_CREDIT, 1, 2));
        gradebook.addGrade(new Grade(Assessment.EXAM, 2, 5));

        assertFalse(gradebook.canTransferToBudget());
    }

    @Test
    public void testCanTransferToBudget_AllGood() {
        Gradebook gradebook = new Gradebook();
        gradebook.addGrade(new Grade(Assessment.EXAM, 1, 4));
        gradebook.addGrade(new Grade(Assessment.CREDIT, 1, 1));
        gradebook.addGrade(new Grade(Assessment.EXAM, 2, 5));
        gradebook.addGrade(new Grade(Assessment.CREDIT, 2, 1));

        assertTrue(gradebook.canTransferToBudget());
    }

    @Test
    public void testCanTransferToBudget_HasOnlyCurrentGrades() {
        Gradebook gradebook = new Gradebook();
        gradebook.addGrade(new Grade(Assessment.EXAM, 1));
        gradebook.addGrade(new Grade(Assessment.CREDIT, 1));
        gradebook.addGrade(new Grade(Assessment.EXAM, 2, 5));
        gradebook.addGrade(new Grade(Assessment.CREDIT, 2, 1));

        assertTrue(gradebook.canTransferToBudget());
    }

    @Test
    public void testCanGetRedDiploma_EmptyGrades() {
        Gradebook gradebook = new Gradebook();
        assertTrue(gradebook.canGetRedDiploma());
    }

    @Test
    public void testCanGetRedDiploma_UnknownGrades() {
        Gradebook gradebook = new Gradebook();
        gradebook.addGrade(new Grade(Assessment.CREDIT, 1));
        gradebook.addGrade(new Grade(Assessment.EXAM, 2));
        gradebook.addGrade(new Grade(Assessment.EXAM, 4));
        gradebook.addGrade(new Grade(Assessment.FIN_QUAL_WORK_PROT, 8));

        assertTrue(gradebook.canGetRedDiploma());
    }

    @Test
    public void testCanGetRedDiploma_HasBadCredit() {
        Gradebook gradebook = new Gradebook();
        gradebook.addGrade(new Grade(Assessment.CREDIT, 1, 0));
        gradebook.addGrade(new Grade(Assessment.EXAM, 1, 5));

        assertFalse(gradebook.canGetRedDiploma());
    }

    @Test
    public void testCanGetRedDiploma_HasBadFinalQualificationWork() {
        Gradebook gradebook = new Gradebook();
        gradebook.addGrade(new Grade(Assessment.FIN_QUAL_WORK_PROT, 1, 4));

        assertFalse(gradebook.canGetRedDiploma());
    }

    @Test
    public void testCanGetRedDiploma_HasBadExam() {
        Gradebook gradebook = new Gradebook();
        gradebook.addGrade(new Grade(Assessment.EXAM, 1, 3));

        assertFalse(gradebook.canGetRedDiploma());
    }

    @Test
    public void testCanGetRedDiploma_HasBadDiffCredit() {
        Gradebook gradebook = new Gradebook();
        gradebook.addGrade(new Grade(Assessment.DIFF_CREDIT, 1, 3));

        assertFalse(gradebook.canGetRedDiploma());
    }

    @Test
    public void testCanGetRedDiploma_AverageLessThan475() {
        Gradebook gradebook = new Gradebook();
        gradebook.addGrade(new Grade(Assessment.EXAM, 1, 4));
        gradebook.addGrade(new Grade(Assessment.EXAM, 1, 5));
        gradebook.addGrade(new Grade(Assessment.CREDIT, 1, 1));

        assertFalse(gradebook.canGetRedDiploma());
    }

    @Test
    public void testCanGetRedDiploma_AverageAtLeast475() {
        Gradebook gradebook = new Gradebook();
        gradebook.addGrade(new Grade(Assessment.EXAM, 1, 5));
        gradebook.addGrade(new Grade(Assessment.EXAM, 1, 5));
        gradebook.addGrade(new Grade(Assessment.EXAM, 1, 5));
        gradebook.addGrade(new Grade(Assessment.EXAM, 1, 4));
        gradebook.addGrade(new Grade(Assessment.CREDIT, 1, 1));

        assertTrue(gradebook.canGetRedDiploma());
    }

    @Test
    public void testCanGetIncreasedScholarship_EmptyGrades() {
        Gradebook gradebook = new Gradebook();
        assertTrue(gradebook.canGetIncreasedScholarship());
    }

    @Test
    public void testCanGetIncreasedScholarship_CurrentSemesterBadExam() {
        Gradebook gradebook = new Gradebook();
        gradebook.addGrade(new Grade(Assessment.EXAM, 1, 3));

        assertFalse(gradebook.canGetIncreasedScholarship());
    }

    @Test
    public void testCanGetIncreasedScholarship_CurrentSemesterBadCredit() {
        Gradebook gradebook = new Gradebook();
        gradebook.addGrade(new Grade(Assessment.CREDIT, 1, 0));

        assertFalse(gradebook.canGetIncreasedScholarship());
    }

    @Test
    public void testCanGetIncreasedScholarship_CurrentSemesterBadDiffCredit() {
        Gradebook gradebook = new Gradebook();
        gradebook.addGrade(new Grade(Assessment.DIFF_CREDIT, 1, 2));

        assertFalse(gradebook.canGetIncreasedScholarship());
    }

    @Test
    public void testCanGetIncreasedScholarship_CurrentSemesterAllGood() {
        Gradebook gradebook = new Gradebook();
        gradebook.addGrade(new Grade(Assessment.EXAM, 1, 4));
        gradebook.addGrade(new Grade(Assessment.CREDIT, 1, 1));
        gradebook.addGrade(new Grade(Assessment.DIFF_CREDIT, 1, 5));

        assertTrue(gradebook.canGetIncreasedScholarship());
    }
}