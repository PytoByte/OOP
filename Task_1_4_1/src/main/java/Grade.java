/**
 * Student grade.
 */
public class Grade {
    private boolean knownGrade;
    public final Assessment assessment;
    public final int semester;
    private int grade;
    public final String name;

    /**
     * Basic grade constructor.
     *
     * @param assessment type of assessment
     * @param semester number of semester
     * @param grade grade
     *
     * @throws IllegalArgumentException if grade or semester not in available range
     * @throws NullPointerException if assessment or name is null
     */
    public Grade(Assessment assessment, int semester, int grade, String name) {
        this.assessment = assessment;
        this.semester = semester;
        this.grade = grade;
        knownGrade = true;
        this.name = name;

        if (assessment == null) {
            throw new NullPointerException("assessment must be nonnull");
        }

        if (name == null) {
            throw new NullPointerException("name must be nonnull");
        }

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
     * @throws NullPointerException if assessment or name is null
     */
    public Grade(Assessment assessment, int semester, String name) {
        this.assessment = assessment;
        this.semester = semester;
        this.grade = -1;
        knownGrade = false;
        this.name = name;

        if (assessment == null) {
            throw new NullPointerException("assessment must be nonnull");
        }

        if (name == null) {
            throw new NullPointerException("name must be nonnull");
        }

        if (semester < 1 || semester > 8) {
            throw new IllegalArgumentException("Semester not in available range (1-8)");
        }
    }

    /**
     * Checks if grade in known.
     *
     * @return true if yes, false overwise
     */
    public boolean isKnownGrade() {
        return knownGrade;
    }

    /**
     * Get the grade value.
     *
     * @return grade value
     * @throws IllegalStateException if grade in unknown
     */
    public int getGrade() {
        if (!isKnownGrade()) {
            throw new IllegalStateException("Grade is unknown");
        }

        return grade;
    }
}
