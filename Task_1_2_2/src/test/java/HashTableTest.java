import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
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
    void putAll() {
        Map<String, Integer> sourceMap = new HashMap<>();
        sourceMap.put("a", 1);
        sourceMap.put("b", 2);
        sourceMap.put("c", 3);

        HashTable<String, Integer> table = new HashTable<>();
        table.putAll(sourceMap);

        assertEquals(3, table.size());
        assertTrue(table.containsKey("a"));
        assertTrue(table.containsKey("b"));
        assertTrue(table.containsKey("c"));
        assertEquals(Integer.valueOf(1), table.get("a"));
        assertEquals(Integer.valueOf(2), table.get("b"));
        assertEquals(Integer.valueOf(3), table.get("c"));
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

        table.remove("two");
        assertEquals(2, keySet.size());
        assertFalse(keySet.contains("two"));
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

        table.put(4, "first");
        assertEquals(4, values.size());
        assertTrue(values.contains("first"));
    }

    @Test
    void entrySet() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("first", 1);
        table.put("second", 2);
        table.put("third", 3);

        Set<Map.Entry<String, Integer>> entrySet = table.entrySet();
        assertEquals(3, entrySet.size());

        boolean foundFirst = false, foundSecond = false, foundThird = false;
        for (Map.Entry<String, Integer> entry : entrySet) {
            if ("first".equals(entry.getKey()) && Integer.valueOf(1).equals(entry.getValue())) {
                foundFirst = true;
            }

            if ("second".equals(entry.getKey()) && Integer.valueOf(2).equals(entry.getValue())) {
                foundSecond = true;
            }
            if ("third".equals(entry.getKey()) && Integer.valueOf(3).equals(entry.getValue())) {
                foundThird = true;
            }
        }
        assertTrue(foundFirst);
        assertTrue(foundSecond);
        assertTrue(foundThird);
    }
}