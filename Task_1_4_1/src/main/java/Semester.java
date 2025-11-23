import java.util.HashMap;
import java.util.Map;

public class Semester {
    private final Map<String, Subject> subjects = new HashMap<>();

    public void addSubject(String name, Subject subject) {
        subjects.put(name, subject);
    }

    public Subject getSubject(String name) {
        return subjects.get(name);
    }

    public Map<Subject.Assessment, Integer> getAssessmentPlan() {
        Map<Subject.Assessment, Integer> plan = new HashMap<>();

        for (Subject.Assessment assessment : Subject.Assessment.values()) {
            plan.put(assessment, 0);
        }

        for (Subject subject : subjects.values()) {
            plan.put(
                    subject.getAssessment(),
                    plan.get(subject.getAssessment()) + 1
            );
        }

        return plan;
    }
}
