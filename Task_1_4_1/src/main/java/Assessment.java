/**
 * Types of assessment.
 */
public enum Assessment {
    EXAM("Экзамен", 2, 5),
    DIFF_CREDIT("Дифференцированный зачёт", 2, 5),
    CREDIT("Зачёт", 0, 1),
    PRAC_REP_PROT("Защита отчёта по практике", 2, 5),
    FIN_QUAL_WORK_PROT("Защита ВРК", 2, 5);

    public final String name;
    public final int min;
    public final int max;

    Assessment(String name, int min, int max) {
        this.name = name;
        this.min = min;
        this.max = max;
    }
}