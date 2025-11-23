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

        Subject.Assessment[][] subjectTypes = {
                {Subject.Assessment.EXAM, Subject.Assessment.DIFF_CREDIT},
                {Subject.Assessment.CREDIT, Subject.Assessment.DIFF_CREDIT},
                {Subject.Assessment.CREDIT, Subject.Assessment.PRAC_REP_PROT}
        };

        for (int i = 0; i < 3; i++) {
            Semester semester = new Semester();
            for (int j = 0; j < subjects[i].length; j++) {
                semester.addSubject(subjects[i][j], new Subject(subjectTypes[i][j]));
            }
            gradebook.addSemester(semester);
        }

        System.out.println(gradebook);
    }
}
