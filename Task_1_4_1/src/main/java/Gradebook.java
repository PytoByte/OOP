import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Student grade book class.
 */
public class Gradebook {
    private final List<Grade> grades = new LinkedList<>();

    /**
     * Add grade into grades list.
     *
     * @param grade some grade
     */
    public void addGrade(Grade grade) {
        grades.add(grade);
    }

    /**
     * Get average grade of all grades.
     *
     * @return average grade of all grades
     */
    public double getCurrentAverageGrade() {
        return grades.stream()
                .filter(grade -> grade.assessment != Assessment.CREDIT && grade.isKnownGrade())
                .mapToDouble(Grade::getGrade)
                .average()
                .orElse(0.0);
    }

    /**
     * Checks if student can be potentially transfer to budget.
     *
     * @return true if yes, false overwise
     */
    public boolean canTransferToBudget() {
        List<Integer> availableSemesters = getAvailableSemesters();

        if (availableSemesters.isEmpty()) {
            return false;
        }

        if (availableSemesters.size() < 2) {
            return false;
        }

        int maxSemester = availableSemesters.stream()
                .max(Integer::compareTo)
                .orElse(0);

        List<Grade> currentSemesterGrades = grades.stream()
                .filter(grade -> grade.semester == maxSemester)
                .toList();

        if (!gradesAreGoodForBudget(currentSemesterGrades)) {
            return false;
        }

        int prevSemesterNum = maxSemester - 1;

        // if no grades for previous semester, we assume that they are all maximal
        if (!availableSemesters.contains(prevSemesterNum)) {
            return true;
        }

        List<Grade> prevSemesterGrades = grades.stream()
                .filter(grade -> grade.semester == prevSemesterNum)
                .toList();

        return gradesAreGoodForBudget(prevSemesterGrades);
    }

    /**
     * Checks if student can potentially get red diploma.
     *
     * @return true if yes, false overwise
     */
    public boolean canGetRedDiploma() {
        if (grades.isEmpty()) {
            return true;
        }

        if (!gradesAreGoodForRedDiploma(grades)) {
            return false;
        }

        List<Integer> knownGrades = grades.stream()
                .filter(grade -> grade.assessment != Assessment.CREDIT && grade.isKnownGrade())
                .mapToInt(Grade::getGrade)
                .boxed()
                .toList();

        long unknownGradesCount = grades.stream()
                .filter(grade -> grade.assessment != Assessment.CREDIT && !grade.isKnownGrade())
                .count();

        int knownGradesSum = 0;
        for (int knownGrade : knownGrades) {
            knownGradesSum += knownGrade;
        }

        double average = (double) (knownGradesSum + unknownGradesCount * 5)
                / (knownGrades.size() + unknownGradesCount);

        return average >= 4.75;
    }

    /**
     * Checks if student can potentially get increased scholarship.
     *
     * @return true if yes, false overwise
     */
    public boolean canGetIncreasedScholarship() {
        List<Integer> availableSemesters = getAvailableSemesters();

        if (availableSemesters.isEmpty()) {
            return true;
        }

        int maxSemester = availableSemesters.stream()
                .max(Integer::compareTo)
                .orElse(0);

        List<Grade> currentSemesterGrades = grades.stream()
                .filter(grade -> grade.semester == maxSemester)
                .toList();

        return gradesAreGoodForIncreasedScholarship(currentSemesterGrades);
    }

    /**
     * Checks if grades in list are good for budget.
     *
     * @param someGrades list of grades
     * @return true if yes, false overwise
     */
    private boolean gradesAreGoodForBudget(List<Grade> someGrades) {
        return someGrades.stream()
                .filter(grade -> grade.assessment == Assessment.EXAM && grade.isKnownGrade())
                .noneMatch(grade -> grade.getGrade() < 4)
                && someGrades.stream()
                .filter(grade -> grade.assessment == Assessment.CREDIT && grade.isKnownGrade())
                .noneMatch(grade -> grade.getGrade() == 0)
                && someGrades.stream()
                .filter(grade -> grade.assessment != Assessment.EXAM
                        && grade.assessment != Assessment.CREDIT && grade.isKnownGrade())
                .noneMatch(grade -> grade.getGrade() < 3);
    }

    /**
     * Checks if grades in list are good for getting red diploma.
     *
     * @param someGrades list of grades
     * @return true if yes, false overwise
     */
    private boolean gradesAreGoodForRedDiploma(List<Grade> someGrades) {
        return someGrades.stream()
                .filter(grade -> grade.assessment == Assessment.CREDIT && grade.isKnownGrade())
                .noneMatch(grade -> grade.getGrade() == 0)
                && someGrades.stream()
                .filter(grade -> grade.assessment == Assessment.FIN_QUAL_WORK_PROT
                        && grade.isKnownGrade())
                .noneMatch(grade -> grade.getGrade() < 5)
                && someGrades.stream()
                .filter(grade -> grade.assessment != Assessment.CREDIT
                        && grade.assessment != Assessment.FIN_QUAL_WORK_PROT
                        && grade.isKnownGrade())
                .noneMatch(grade -> grade.getGrade() < 4);
    }

    /**
     * Checks if grades in list are good for getting increased scholarship.
     *
     * @param someGrades list of grades
     * @return true if yes, false overwise
     */
    private boolean gradesAreGoodForIncreasedScholarship(List<Grade> someGrades) {
        return someGrades.stream()
                .filter(grade -> grade.assessment != Assessment.CREDIT && grade.isKnownGrade())
                .noneMatch(grade -> grade.getGrade() < 4)
                && someGrades.stream()
                .filter(grade -> grade.assessment == Assessment.CREDIT && grade.isKnownGrade())
                .noneMatch(grade -> grade.getGrade() == 0);
    }

    /**
     * Get numbers of semesters that has known grades.
     *
     * @return numbers of semesters that has known grades
     */
    private List<Integer> getAvailableSemesters() {
        return grades.stream()
                .filter(Grade::isKnownGrade)
                .map(grade -> grade.semester)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
}