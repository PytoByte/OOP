/**
 * Types of assessment.
 */
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