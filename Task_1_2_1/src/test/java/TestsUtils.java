import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;

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
    public static void assertArraysEqualIgnoreOrder(String[] expected, String[] actual) {
        if (expected == null || actual == null) {
            return;
        }
        assertEquals(expected.length, actual.length);

        HashMap<String, Integer> freq = new HashMap<>();
        for (String s : actual) {
            freq.put(s, freq.getOrDefault(s, 0) + 1);
        }
        for (String s : expected) {
            Integer count = freq.get(s);
            if (count == null) {
                System.out.printf("Expected more \"%s\" elements", s);
                assertNotNull(count);
            }
            if (count - 1 <= 0) {
                freq.remove(s);
            }
        }

        if (!freq.isEmpty()) {
            System.out.println("Not all elements presented in actual array");
            assertTrue(freq.isEmpty());
        }
    }
}
