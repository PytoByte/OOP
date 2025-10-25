/**
 * Contains constants for incidence matrix graph.
 */
public enum IncidenceMatrixValues {
    FROM("-1"), TO("1"), SELF_LOOP("2"), EMPTY("0");

    public final String value;

    IncidenceMatrixValues(String value) {
        this.value = value;
    }
}
