import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
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
    public static <NodeType> void assertListsEqualIgnoreOrder(
            List<NodeType> expected,
            List<NodeType> actual
    ) {
        if (expected == null || actual == null) {
            return;
        }
        assertEquals(expected.size(), actual.size());
        assertEquals(new HashSet<>(expected), new HashSet<>(actual));
    }
}
