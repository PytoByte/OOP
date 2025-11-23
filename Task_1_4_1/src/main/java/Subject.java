public class Subject {
    public enum Assessment {
        EXAM("Экзамен"),
        DIFF_CREDIT("Дифференцированный зачёт"),
        CREDIT("Зачёт"),
        PRAC_REP_PROT("Защита отчёта по практике"),
        FIN_QUAL_WORK_PROT("Защита ВРК");

        public final String name;

        Assessment(String name) {
            this.name = name;
        }
    }

    private final Assessment assessment;

    Subject(Assessment assessment) {
        this.assessment = assessment;
    }

    public Assessment getAssessment() {
        return assessment;
    }

    /*
    test
    colloquium
    EXAM
    DIFF_CREDIT
    CREDIT
    PRAC_REP_PROT
    FIN_QUAL_WORK_PROT
     */
}
