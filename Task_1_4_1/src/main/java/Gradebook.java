import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Gradebook {
    private final List<Grade> grades = new LinkedList<>();

    public void addGrade(Grade grade) {
        grades.add(grade);
    }

    public double getCurrentAverageGrade() {
        return grades.stream()
                .filter(grade -> grade.assessment() != Assessment.CREDIT)
                .mapToDouble(Grade::grade)
                .average()
                .orElse(0.0);
    }

    public boolean canTransferToBudget() {
        List<Integer> availableSemesters = getAvailableSemesters();

        if (availableSemesters.isEmpty()) {
            return true;
        }

        int maxSemester = availableSemesters.stream()
                .max(Integer::compareTo)
                .orElse(0);

        List<Grade> currentSemesterGrades = grades.stream()
                .filter(grade -> grade.semester() == maxSemester)
                .toList();

        if (
                currentSemesterGrades.stream()
                        .filter(grade -> grade.assessment() == Assessment.EXAM)
                        .anyMatch(grade -> grade.grade() < 4)
                        || currentSemesterGrades.stream()
                        .filter(grade -> grade.assessment() == Assessment.CREDIT)
                        .anyMatch(grade -> grade.grade() == 0)
                        || currentSemesterGrades.stream()
                        .filter(grade -> grade.assessment() != Assessment.EXAM
                                && grade.assessment() != Assessment.CREDIT)
                        .anyMatch(grade -> grade.grade() < 3)
        ) {
            return false;
        }


        if (maxSemester != 1) {
            List<Grade> prevSemesterGrades = grades.stream()
                    .filter(grade -> grade.semester() == maxSemester - 1)
                    .toList();

            if (!prevSemesterGrades.isEmpty()) {
                return prevSemesterGrades.stream()
                        .filter(grade -> grade.assessment() == Assessment.EXAM)
                        .allMatch(grade -> grade.grade() >= 4)
                        && prevSemesterGrades.stream()
                        .filter(grade -> grade.assessment() == Assessment.CREDIT)
                        .allMatch(grade -> grade.grade() == 1)
                        && prevSemesterGrades.stream()
                        .filter(grade -> grade.assessment() != Assessment.EXAM
                                && grade.assessment() != Assessment.CREDIT)
                        .allMatch(grade -> grade.grade() >= 3);
            }
        }

        return true;
    }

    public boolean canGetRedDiploma() {
        if (grades.isEmpty()) {
            return true;
        }

        if (grades.stream()
                .filter(grade -> grade.assessment() == Assessment.CREDIT)
                .anyMatch(grade -> grade.grade() == 0)
                || grades.stream()
                .filter(grade -> grade.assessment() == Assessment.FIN_QUAL_WORK_PROT)
                .anyMatch(grade -> grade.grade() < 5)
                || grades.stream()
                .filter(grade -> grade.assessment() != Assessment.CREDIT
                        && grade.assessment() != Assessment.FIN_QUAL_WORK_PROT)
                .anyMatch(grade -> grade.grade() < 4)
        ) {
            return false;
        }

        double aver = grades.stream()
                .filter(grade -> grade.assessment() != Assessment.CREDIT)
                .mapToDouble(Grade::grade)
                .average()
                .orElse(0.0);

        return aver >= 4.75;
    }

    public boolean canGetIncreasedScholarship() {
        List<Integer> availableSemesters = getAvailableSemesters();

        if (availableSemesters.isEmpty()) {
            return true;
        }

        int maxSemester = availableSemesters.stream()
                .max(Integer::compareTo)
                .orElse(0);

        List<Grade> currentSemesterGrades = grades.stream()
                .filter(grade -> grade.semester() == maxSemester)
                .toList();

        return currentSemesterGrades.stream()
                .filter(grade -> grade.assessment() != Assessment.CREDIT)
                .allMatch(grade -> grade.grade() >= 4)
                && currentSemesterGrades.stream()
                .filter(grade -> grade.assessment() == Assessment.CREDIT)
                .allMatch(grade -> grade.grade() == 1);
    }

    private List<Integer> getAvailableSemesters() {
        return grades.stream()
                .map(Grade::semester)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
}