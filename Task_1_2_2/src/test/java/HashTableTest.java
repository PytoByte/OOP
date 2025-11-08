import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.junit.jupiter.api.Test;

class HashTableTest {

    @Test
    void size() {
        HashTable<String, String> table = new HashTable<>();
        table.put("Hello", "World");
        assertEquals(1, table.size());
        table.put("System out", "println");
        assertEquals(2, table.size());
        table.remove("Hello");
        assertEquals(1, table.size());
    }

    @Test
    void isEmpty() {
        HashTable<Integer, Object> table = new HashTable<>();
        assertTrue(table.isEmpty());
        table.put(10, new HashTable<>());
        assertFalse(table.isEmpty());
    }

    @Test
    void containsKey() {
        HashTable<String, Boolean> table = new HashTable<>();
        assertFalse(table.containsKey("test"));
        table.put("test", true);
        assertTrue(table.containsKey("test"));
        assertFalse(table.containsKey("test1"));
    }

    @Test
    void containsValue() {
        HashTable<Integer, Double> table = new HashTable<>();
        assertFalse(table.containsValue(0.2d));
        table.put(1, 0.2d);
        assertTrue(table.containsValue(0.2d));
        assertFalse(table.containsValue(0.3d));
    }

    @Test
    void get() {
        HashTable<String, String> table = new HashTable<>();
        table.put("hello", "world");
        assertEquals("world", table.get("hello"));
        assertNull(table.get("world"));
    }

    @Test
    void put() {
        HashTable<String, String> table = new HashTable<>();
        assertNull(table.put("hello", "overworld"));
        assertEquals("overworld", table.get("hello"));
        assertEquals("overworld", table.put("hello", "netherworld"));
        assertEquals("netherworld", table.get("hello"));
    }

    @Test
    void putNullKey() {
        HashTable<String, String> table = new HashTable<>();
        assertThrows(IllegalArgumentException.class, () -> table.put(null, "null_value"));
    }

    @Test
    void putAll() {
        Map<String, Integer> source = new HashMap<>();
        source.put("a", 1);
        source.put("b", 2);
        source.put("c", 3);

        HashTable<String, Integer> table = new HashTable<>();
        table.putAll(source);

        assertEquals(source.size(), table.size());
        for (Map.Entry<String, Integer> entry : table.entrySet()) {
            Integer elem = source.remove(entry.getKey());
            assertNotNull(elem);
            assertEquals(elem, entry.getValue());
        }
    }

    @Test
    void remove() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("key1", 100);
        table.put("key2", 200);

        assertEquals(Integer.valueOf(100), table.remove("key1"));
        assertEquals(1, table.size());
        assertFalse(table.containsKey("key1"));
        assertNull(table.get("key1"));

        assertNull(table.remove("nonexistent"));
        assertEquals(1, table.size());

        assertEquals(Integer.valueOf(200), table.remove("key2"));
        assertEquals(0, table.size());
        assertTrue(table.isEmpty());
    }

    @Test
    void clear() {
        HashTable<String, String> table = new HashTable<>();
        table.put("key1", "value1");
        table.put("key2", "value2");
        assertFalse(table.isEmpty());
        assertEquals(2, table.size());

        table.clear();
        assertTrue(table.isEmpty());
        assertEquals(0, table.size());
        assertNull(table.get("key1"));
        assertNull(table.get("key2"));
    }

    @Test
    void keySet() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);
        table.put("two", 2);
        table.put("three", 3);

        Set<String> keySet = table.keySet();
        assertEquals(3, keySet.size());
        assertTrue(keySet.contains("one"));
        assertTrue(keySet.contains("two"));
        assertTrue(keySet.contains("three"));
    }

    @Test
    void values() {
        HashTable<Integer, String> table = new HashTable<>();
        table.put(1, "first");
        table.put(2, "second");
        table.put(3, "third");

        Collection<String> values = table.values();
        assertEquals(3, values.size());
        assertTrue(values.contains("first"));
        assertTrue(values.contains("second"));
        assertTrue(values.contains("third"));
    }

    @Test
    void resize() {
        HashTable<String, String> table = new HashTable<>(2);
        table.put("key1", "val1");
        assertEquals(1, table.size());

        table.put("key2", "val2");
        assertEquals(2, table.size());
        assertTrue(table.containsKey("key1"));
        assertTrue(table.containsKey("key2"));
    }

    @Test
    void handleCollisions() {
        record BadHashKey(String value) {
            @Override
            public int hashCode() {
                return 42; // Постоянное значение -> коллизии
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (o == null || getClass() != o.getClass()) {
                    return false;
                }
                BadHashKey that = (BadHashKey) o;
                return Objects.equals(value, that.value);
            }
        }

        HashTable<BadHashKey, String> table = new HashTable<>();
        BadHashKey key1 = new BadHashKey("key1");
        BadHashKey key2 = new BadHashKey("key2");
        BadHashKey key3 = new BadHashKey("key3");

        table.put(key1, "value1");
        table.put(key2, "value2");
        table.put(key3, "value3");

        assertEquals("value1", table.get(key1));
        assertEquals("value2", table.get(key2));
        assertEquals("value3", table.get(key3));

        assertEquals(3, table.size());
        assertTrue(table.containsKey(key1));
        assertTrue(table.containsKey(key2));
        assertTrue(table.containsKey(key3));
    }

    @Test
    void entrySet() {
        HashTable<String, Integer> table = new HashTable<>();
        Map<String, Integer> expected = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            String key = "key" + i;
            Integer val = i;
            table.put(key, val);
            expected.put(key, val);
        }

        for (Map.Entry<String, Integer> entry : table.entrySet()) {
            Integer elem = expected.remove(entry.getKey());
            assertNotNull(elem);
            assertEquals(elem, entry.getValue());
        }
        assertTrue(expected.isEmpty());
    }

    @Test
    void entrySetSize() {
        HashTable<String, Integer> table = new HashTable<>();
        int expectedSize = 10;

        for (int i = 0; i < expectedSize; i++) {
            String key = "key" + i;
            Integer val = i;
            table.put(key, val);
        }

        assertEquals(expectedSize, table.size());
    }

    @Test
    void iteratorConcurrentModificationExceptionOnPut() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("key1", 1);
        table.put("key2", 2);

        Iterator<Map.Entry<String, Integer>> it = table.entrySet().iterator();

        table.put("key3", 3);

        assertThrows(ConcurrentModificationException.class, it::hasNext);
    }

    @Test
    void iteratorConcurrentModificationExceptionOnRemove() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("key1", 1);
        table.put("key2", 2);

        Iterator<Map.Entry<String, Integer>> it = table.entrySet().iterator();

        table.remove("key1");

        assertThrows(ConcurrentModificationException.class, it::hasNext);
    }

    @Test
    void iteratorConcurrentModificationExceptionOnClear() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("key1", 1);
        table.put("key2", 2);

        Iterator<Map.Entry<String, Integer>> it = table.entrySet().iterator();

        table.clear();

        assertThrows(ConcurrentModificationException.class, it::hasNext);
    }

    @Test
    void iteratorConcurrentModificationExceptionAfterNext() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("key1", 1);
        table.put("key2", 2);

        Iterator<Map.Entry<String, Integer>> it = table.entrySet().iterator();

        it.next();

        table.put("key3", 3);

        assertThrows(ConcurrentModificationException.class, it::next);
    }

    @Test
    void iteratorConcurrentModificationExceptionWhileIteration() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("key1", 1);
        table.put("key2", 2);
        assertThrows(ConcurrentModificationException.class, () -> {
            for (Map.Entry<String, Integer> entry : table.entrySet()) {
                table.put("key3", 3);
            }
        });
    }

    @Test
    void toStringTest() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("key1", 1);
        table.put("key2", 2);

        assertTrue(
                "{key1:1, key2:2}".equals(table.toString())
                || "{key2:2, key1:1}".equals(table.toString())
        );
    }

    @Test
    void equalsTest() {
        HashTable<String, Integer> table1 = new HashTable<>();
        HashTable<String, Integer> table2 = new HashTable<>();

        table1.put("key1", 1);
        table1.put("key2", 2);

        table2.put("key1", 1);
        table2.put("key2", 2);

        assertEquals(table1, table2);
    }

    @Test
    void notEqualsTest() {
        HashTable<String, Integer> table1 = new HashTable<>();
        HashTable<String, Integer> table2 = new HashTable<>();

        table1.put("key1", 1);
        table1.put("key2", 2);

        table2.put("key3", 3);
        table2.put("key4", 4);

        assertNotEquals(table1, table2);
    }

    @Test
    void notEqualHashcode() {
        HashTable<String, Integer> table1 = new HashTable<>();
        HashTable<String, Integer> table2 = new HashTable<>();

        table1.put("key1", 1);
        table1.put("key2", 2);

        table2.put("key3", 3);
        table2.put("key4", 4);

        assertNotEquals(table1.hashCode(), table2.hashCode());
    }
}