/**
 * Student grade.
 */
public class Grade {
    private boolean knownGrade;
    public final Assessment assessment;
    public final int semester;
    private int grade;

    /**
     * Basic grade constructor.
     *
     * @param assessment type of assessment
     * @param semester number of semester
     * @param grade grade
     *
     * @throws IllegalArgumentException if grade or semester not in available range
     */
    public Grade(Assessment assessment, int semester, int grade) {
        this.assessment = assessment;
        this.semester = semester;
        this.grade = grade;
        knownGrade = true;

        if (assessment == Assessment.CREDIT) {
            if (grade < 0 || grade > 1) {
                throw new IllegalArgumentException("Grade not in assessment available range (0-1)");
            }
        } else if (grade < 2 || grade > 5) {
            throw new IllegalArgumentException("Grade not in assessment available range (2-5)");
        }

        if (semester < 1 || semester > 8) {
            throw new IllegalArgumentException("Semester not in available range (1-8)");
        }
    }

    /**
     * Grade constructor with unknown grade.
     *
     * @param assessment type of assessment
     * @param semester number of semester
     *
     * @throws IllegalArgumentException if semester not in available range
     */
    public Grade(Assessment assessment, int semester) {
        this.assessment = assessment;
        this.semester = semester;
        this.grade = -1;
        knownGrade = false;

        if (semester < 1 || semester > 8) {
            throw new IllegalArgumentException("Semester not in available range (1-8)");
        }
    }

    public boolean isKnownGrade() {
        return knownGrade;
    }

    public int getGrade() {
        if (!isKnownGrade()) {
            throw new IllegalStateException("Grade is unknown");
        }

        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
        knownGrade = true;
    }
}
