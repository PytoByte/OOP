import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
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
        int currentSemesterNum = getCurrentSemester();

        if (currentSemesterNum < 2) {
            return false;
        }

        List<Grade> currentSemesterGrades = grades.stream()
                .filter(grade -> grade.semester == currentSemesterNum)
                .toList();

        if (!gradesAreGoodForBudget(currentSemesterGrades)) {
            return false;
        }

        int prevSemesterNum = currentSemesterNum - 1;
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

        Collection<Grade> lastGrades = getLastGrades(grades).stream()
                .filter(grade -> grade.assessment != Assessment.CREDIT)
                .toList();

        int sumLastGrades = 0;
        for (Grade grade : lastGrades) {
            if (grade.isKnownGrade()) {
                sumLastGrades += grade.getGrade();
            } else {
                sumLastGrades += 5;
            }
        }

        double average = (double) sumLastGrades / lastGrades.size();

        return average >= 4.75;
    }

    /**
     * Checks if student can potentially get increased scholarship.
     *
     * @return true if yes, false overwise
     */
    public boolean canGetIncreasedScholarship() {
        if (grades.isEmpty()) {
            return true;
        }

        int currentSemesterNum = getCurrentSemester();

        List<Grade> currentSemesterGrades = grades.stream()
                .filter(grade -> grade.semester == currentSemesterNum)
                .toList();

        return gradesAreGoodForIncreasedScholarship(currentSemesterGrades);
    }

    /**
     * Get last grades by subject, no matter known or unknown.
     *
     * @param someGrades list of grades
     * @return list of last subject grades
     */
    private Collection<Grade> getLastGrades(List<Grade> someGrades) {
        return someGrades.stream()
                .collect(Collectors.toMap(
                        grade -> grade.name,
                        grade -> grade,
                        (existing, replacement) ->
                                replacement.semester >= existing.semester ? replacement : existing))
                .values();
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
     * Get current semester number.
     *
     * @return numbers of current semester
     */
    private int getCurrentSemester() {
        return grades.stream()
                .filter(Grade::isKnownGrade)
                .map(grade -> grade.semester)
                .max(Integer::compareTo).orElse(1);
    }
}