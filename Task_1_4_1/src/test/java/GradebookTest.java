import org.junit.jupiter.api.Test;

public class GradebookTest {
    @Test
    public void mainTest() {
        Gradebook gradebook = new Gradebook();
        String[][] subjects = {
                {"Матанализ", "C"},
                {"Английский", "SQL"},
                {"ОСи", "РПАК"}
        };

        Assessment[][] subjectTypes = {
                {Assessment.EXAM, Assessment.DIFF_CREDIT},
                {Assessment.CREDIT, Assessment.DIFF_CREDIT},
                {Assessment.CREDIT, Assessment.PRAC_REP_PROT}
        };

        int[][] subjectGrades = {
                {3, 5},
                {1, 5},
                {0, 4}
        };

        for (int i = 0; i < 3; i++) {
            Semester semester = new Semester();
            for (int j = 0; j < subjects[i].length; j++) {
                semester.addSubject(new Grade(subjects[i][j], subjectTypes[i][j], subjectGrades[i][j]));
            }
            gradebook.addSemester(semester);
        }

        System.out.println(gradebook);
        System.out.println(gradebook.getAverGrade());
    }
}
