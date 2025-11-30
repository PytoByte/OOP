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
        gradebook.addGrade(new Grade(Assessment.EXAM, 1, 4, "Mathematics"));
        gradebook.addGrade(new Grade(Assessment.CREDIT, 1, 1, "Physical Education"));
        gradebook.addGrade(new Grade(Assessment.EXAM, 1, 5, "Physics"));

        assertEquals(4.5, gradebook.getCurrentAverageGrade(), 0.001);
    }

    @Test
    public void testGetCurrentAverageGrade_NoCredits() {
        Gradebook gradebook = new Gradebook();
        gradebook.addGrade(new Grade(Assessment.EXAM, 1, 4, "Chemistry"));
        gradebook.addGrade(new Grade(Assessment.DIFF_CREDIT, 1, 3, "Biology"));

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
        gradebook.addGrade(new Grade(Assessment.EXAM, 1, 5, "Algebra"));

        assertFalse(gradebook.canTransferToBudget());
    }

    @Test
    public void testCanTransferToBudget_CurrentSemesterHasBadExam() {
        Gradebook gradebook = new Gradebook();
        gradebook.addGrade(new Grade(Assessment.EXAM, 1, 5, "History"));
        gradebook.addGrade(new Grade(Assessment.EXAM, 2, 3, "Philosophy"));

        assertFalse(gradebook.canTransferToBudget());
    }

    @Test
    public void testCanTransferToBudget_CurrentSemesterHasBadCredit() {
        Gradebook gradebook = new Gradebook();
        gradebook.addGrade(new Grade(Assessment.CREDIT, 1, 1, "Economics"));
        gradebook.addGrade(new Grade(Assessment.CREDIT, 2, 0, "Sociology"));

        assertFalse(gradebook.canTransferToBudget());
    }

    @Test
    public void testCanTransferToBudget_CurrentSemesterHasBadDiffCredit() {
        Gradebook gradebook = new Gradebook();
        gradebook.addGrade(new Grade(Assessment.CREDIT, 1, 1, "Law"));
        gradebook.addGrade(new Grade(Assessment.DIFF_CREDIT, 2, 2, "Political Science"));

        assertFalse(gradebook.canTransferToBudget());
    }

    @Test
    public void testCanTransferToBudget_CurrentSemesterHasBadPracticeReport() {
        Gradebook gradebook = new Gradebook();
        gradebook.addGrade(new Grade(Assessment.CREDIT, 1, 1, "Programming"));
        gradebook.addGrade(new Grade(Assessment.PRAC_REP_PROT, 2, 2, "Internship Report"));

        assertFalse(gradebook.canTransferToBudget());
    }

    @Test
    public void testCanTransferToBudget_PreviousSemesterBadExam() {
        Gradebook gradebook = new Gradebook();
        gradebook.addGrade(new Grade(Assessment.EXAM, 1, 3, "Calculus"));
        gradebook.addGrade(new Grade(Assessment.EXAM, 2, 5, "Differential Equations"));

        assertFalse(gradebook.canTransferToBudget());
    }

    @Test
    public void testCanTransferToBudget_PreviousSemesterBadCredit() {
        Gradebook gradebook = new Gradebook();
        gradebook.addGrade(new Grade(Assessment.CREDIT, 1, 0, "Art History"));
        gradebook.addGrade(new Grade(Assessment.EXAM, 2, 5, "Literature"));

        assertFalse(gradebook.canTransferToBudget());
    }

    @Test
    public void testCanTransferToBudget_PreviousSemesterBadDiffCredit() {
        Gradebook gradebook = new Gradebook();
        gradebook.addGrade(new Grade(Assessment.DIFF_CREDIT, 1, 2, "Statistics"));
        gradebook.addGrade(new Grade(Assessment.EXAM, 2, 5, "Probability Theory"));

        assertFalse(gradebook.canTransferToBudget());
    }

    @Test
    public void testCanTransferToBudget_AllGood() {
        Gradebook gradebook = new Gradebook();
        gradebook.addGrade(new Grade(Assessment.EXAM, 1, 4, "Computer Science"));
        gradebook.addGrade(new Grade(Assessment.CREDIT, 1, 1, "Ethics"));
        gradebook.addGrade(new Grade(Assessment.EXAM, 2, 5, "Algorithms"));
        gradebook.addGrade(new Grade(Assessment.CREDIT, 2, 1, "Psychology"));

        assertTrue(gradebook.canTransferToBudget());
    }

    @Test
    public void testCanTransferToBudget_HasOnlyCurrentGrades() {
        Gradebook gradebook = new Gradebook();
        gradebook.addGrade(new Grade(Assessment.EXAM, 1, "Linear Algebra"));
        gradebook.addGrade(new Grade(Assessment.CREDIT, 1, "Music"));
        gradebook.addGrade(new Grade(Assessment.EXAM, 2, 5, "Discrete Math"));
        gradebook.addGrade(new Grade(Assessment.CREDIT, 2, 1, "Dance"));

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
        gradebook.addGrade(new Grade(Assessment.CREDIT, 1, "Foreign Language"));
        gradebook.addGrade(new Grade(Assessment.EXAM, 2, "Database Systems"));
        gradebook.addGrade(new Grade(Assessment.EXAM, 4, "Software Engineering"));
        gradebook.addGrade(new Grade(Assessment.FIN_QUAL_WORK_PROT, 8, "Diploma Project"));

        assertTrue(gradebook.canGetRedDiploma());
    }

    @Test
    public void testCanGetRedDiploma_LongSubject() {
        Gradebook gradebook = new Gradebook();
        gradebook.addGrade(new Grade(Assessment.EXAM, 2, 5, "Database Systems"));
        gradebook.addGrade(new Grade(Assessment.EXAM, 3, 5, "Database Systems"));
        gradebook.addGrade(new Grade(Assessment.EXAM, 4, 5,  "Software Engineering"));
        gradebook.addGrade(new Grade(Assessment.EXAM, 4, 4,  "Operating Systems"));

        assertFalse(gradebook.canGetRedDiploma());
    }

    @Test
    public void testCanGetRedDiploma_LongSubject_LastUnknownGrade() {
        Gradebook gradebook = new Gradebook();
        gradebook.addGrade(new Grade(Assessment.EXAM, 2, 4, "Database Systems"));
        gradebook.addGrade(new Grade(Assessment.EXAM, 3, "Database Systems"));
        gradebook.addGrade(new Grade(Assessment.EXAM, 4, 5,  "Software Engineering"));
        gradebook.addGrade(new Grade(Assessment.EXAM, 4, 5,  "Operating Systems"));

        assertTrue(gradebook.canGetRedDiploma());
    }

    @Test
    public void testCanGetRedDiploma_HasBadCredit() {
        Gradebook gradebook = new Gradebook();
        gradebook.addGrade(new Grade(Assessment.CREDIT, 1, 0, "Physical Training"));
        gradebook.addGrade(new Grade(Assessment.EXAM, 1, 5, "Operating Systems"));

        assertFalse(gradebook.canGetRedDiploma());
    }

    @Test
    public void testCanGetRedDiploma_HasBadFinalQualificationWork() {
        Gradebook gradebook = new Gradebook();
        gradebook.addGrade(new Grade(Assessment.FIN_QUAL_WORK_PROT, 1, 4, "Graduation Work"));

        assertFalse(gradebook.canGetRedDiploma());
    }

    @Test
    public void testCanGetRedDiploma_HasBadExam() {
        Gradebook gradebook = new Gradebook();
        gradebook.addGrade(new Grade(Assessment.EXAM, 1, 3, "Network Technologies"));

        assertFalse(gradebook.canGetRedDiploma());
    }

    @Test
    public void testCanGetRedDiploma_HasBadDiffCredit() {
        Gradebook gradebook = new Gradebook();
        gradebook.addGrade(new Grade(Assessment.DIFF_CREDIT, 1, 3, "Web Development"));

        assertFalse(gradebook.canGetRedDiploma());
    }

    @Test
    public void testCanGetRedDiploma_AverageLessThan475() {
        Gradebook gradebook = new Gradebook();
        gradebook.addGrade(new Grade(Assessment.EXAM, 1, 4, "Data Structures"));
        gradebook.addGrade(new Grade(Assessment.EXAM, 1, 5, "Computer Architecture"));
        gradebook.addGrade(new Grade(Assessment.CREDIT, 1, 1, "Technical Drawing"));

        assertFalse(gradebook.canGetRedDiploma());
    }

    @Test
    public void testCanGetRedDiploma_AverageAtLeast475() {
        Gradebook gradebook = new Gradebook();
        gradebook.addGrade(new Grade(Assessment.EXAM, 1, 5, "Machine Learning"));
        gradebook.addGrade(new Grade(Assessment.EXAM, 1, 5, "Artificial Intelligence"));
        gradebook.addGrade(new Grade(Assessment.EXAM, 1, 5, "Neural Networks"));
        gradebook.addGrade(new Grade(Assessment.EXAM, 1, 4, "Data Mining"));
        gradebook.addGrade(new Grade(Assessment.CREDIT, 1, 1, "Research Methods"));

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
        gradebook.addGrade(new Grade(Assessment.EXAM, 1, 3, "Compiler Construction"));

        assertFalse(gradebook.canGetIncreasedScholarship());
    }

    @Test
    public void testCanGetIncreasedScholarship_CurrentSemesterBadCredit() {
        Gradebook gradebook = new Gradebook();
        gradebook.addGrade(new Grade(Assessment.CREDIT, 1, 0, "Public Speaking"));

        assertFalse(gradebook.canGetIncreasedScholarship());
    }

    @Test
    public void testCanGetIncreasedScholarship_CurrentSemesterBadDiffCredit() {
        Gradebook gradebook = new Gradebook();
        gradebook.addGrade(new Grade(Assessment.DIFF_CREDIT, 1, 2, "Mobile Development"));

        assertFalse(gradebook.canGetIncreasedScholarship());
    }

    @Test
    public void testCanGetIncreasedScholarship_CurrentSemesterAllGood() {
        Gradebook gradebook = new Gradebook();
        gradebook.addGrade(new Grade(Assessment.EXAM, 1, 4, "Cryptography"));
        gradebook.addGrade(new Grade(Assessment.CREDIT, 1, 1, "Project Management"));
        gradebook.addGrade(new Grade(Assessment.DIFF_CREDIT, 1, 5, "Cloud Computing"));

        assertTrue(gradebook.canGetIncreasedScholarship());
    }
}