import java.util.ArrayList;
import java.util.Map;

public class Gradebook {
    private final ArrayList<Semester> semesters = new ArrayList<>();

    public void addSemester(Semester semester) {
        semesters.add(semester);
    }

    public Semester getSemester(int number) {
        return semesters.get(number);
    }

    @Override
    public String toString() {
        String semesterNameBase = "Семестр ";

        StringBuilder sb = new StringBuilder();

        int assessmentMaxLen = 0;
        for (Subject.Assessment assessment : Subject.Assessment.values()) {
            assessmentMaxLen = Math.max(assessmentMaxLen, assessment.name.length());
        }

        sb.append(" ".repeat(assessmentMaxLen));
        for (int i = 0; i < semesters.size(); i++) {
            sb.append("|");
            String semesterName = semesterNameBase + i;
            sb.append(semesterName);
        }
        sb.append("\n");

        for (Subject.Assessment assessment : Subject.Assessment.values()) {
            sb.append(assessment.name);
            sb.append(" ".repeat(assessmentMaxLen - assessment.name.length()));
            for (int i = 0; i < semesters.size(); i++) {
                Semester semester = semesters.get(i);
                String semesterName = semesterNameBase + i;
                sb.append("|");
                Map<Subject.Assessment, Integer> plan = semester.getAssessmentPlan();
                sb.append(plan.get(assessment));
                sb.append(" ".repeat(semesterName.length() - plan.get(assessment).toString().length()));
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
