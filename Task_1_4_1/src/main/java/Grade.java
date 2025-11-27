public record Grade(Assessment assessment, int semester, int grade) {
    public Grade {
        if (grade < assessment.min || grade > assessment.max) {
            throw new IllegalArgumentException("Grade not in assessment available range");
        }
    }
}
