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

        int maxSemester = availableSemesters.stream().max(Integer::compareTo).orElse(0);

        // Проверяем текущий (последний) семестр
        List<Grade> currentSemesterGrades = grades.stream()
                .filter(grade -> grade.semester() == maxSemester)
                .toList();

        if (!currentSemesterGrades.isEmpty()) {
            // Если есть оценки в текущем семестре, проверяем отсутствие троек за экзамены
            boolean hasSatisfactoryInCurrent = currentSemesterGrades.stream()
                    .filter(grade -> grade.assessment() == Assessment.EXAM)
                    .anyMatch(grade -> grade.grade() == 3);

            if (hasSatisfactoryInCurrent) {
                return false;
            }
        }

        // Проверяем предыдущий семестр (если не восьмой)
        if (maxSemester != 8) {
            List<Grade> prevSemesterGrades = grades.stream()
                    .filter(grade -> grade.semester() == maxSemester - 1)
                    .toList();

            if (!prevSemesterGrades.isEmpty()) {
                // Если есть оценки в предыдущем семестре, проверяем отсутствие троек за экзамены
                boolean hasSatisfactoryInPrev = prevSemesterGrades.stream()
                        .filter(grade -> grade.assessment() == Assessment.EXAM)
                        .anyMatch(grade -> grade.grade() == 3);

                if (hasSatisfactoryInPrev) {
                    return false;
                }
            } else {
                // Если нет оценок за предыдущий семестр, проверяем текущий на отсутствие троек
                if (!currentSemesterGrades.isEmpty()) {
                    boolean hasSatisfactoryInCurrent = currentSemesterGrades.stream()
                            .filter(grade -> grade.assessment() == Assessment.EXAM)
                            .anyMatch(grade -> grade.grade() == 3);

                    return !hasSatisfactoryInCurrent;
                }
            }
        }

        return true;
    }

    public boolean canGetRedDiploma() {
        if (grades.isEmpty()) {
            return true;
        }

        boolean hasSatisfactoryGrades = grades.stream()
                .filter(grade -> grade.assessment() != Assessment.CREDIT) // Исключаем обычные зачеты
                .anyMatch(grade -> grade.grade() < 4);

        if (hasSatisfactoryGrades) {
            return false;
        }

        long excellentCount = grades.stream()
                .map(Grade::assessment)
                .filter(assessment -> assessment != Assessment.CREDIT)
                .distinct()
                .count();

        long excellentGrades = grades.stream()
                .filter(grade -> grade.assessment() != Assessment.CREDIT && grade.grade() == 5)
                .count();

        if (excellentCount > 0 && (double) excellentGrades / excellentCount < 0.75) {
            return false;
        }

        return grades.stream()
                .filter(grade -> grade.assessment() == Assessment.FIN_QUAL_WORK_PROT)
                .anyMatch(grade -> grade.grade() == 5);
    }

    public boolean canGetIncreasedScholarship(int currentSemester) {
        List<Grade> currentSemesterGrades = grades.stream()
                .filter(grade -> grade.semester() == currentSemester)
                .toList();

        if (currentSemesterGrades.isEmpty()) {
            return true; // Если нет оценок в семестре, считаем что можно
        }

        // Проверяем, что все оценки в текущем семестре >= 4 (не "удовлетворительно")
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