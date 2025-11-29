public record Grade(Assessment assessment, int semester, int grade) {
    public Grade {
        if (grade < assessment.min || grade > assessment.max) {
            String s = String.format("Grade not in assessment available range (%d-%d)",
                    assessment.min, assessment.max);
            throw new IllegalArgumentException(s);
        }

        if (semester < 1 || semester > 8) {
            throw new IllegalArgumentException("Semester not in available range (1-8)");
        }
    }
}
