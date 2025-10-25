import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.List;

/**
 * Utils for graph testing.
 */
public class TestsUtils {
    private TestsUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Assert arrays equal with ignoring order.
     *
     * @param expected expected array
     * @param actual actual array
     */
    public static <T> void assertListsEqualIgnoreOrder(
            List<T> expected,
            List<T> actual
    ) {
        if (expected == null || actual == null) {
            return;
        }
        assertEquals(expected.size(), actual.size());
        assertEquals(new HashSet<>(expected), new HashSet<>(actual));
    }
}
