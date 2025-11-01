import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashTableTest {

    @Test
    void size() {
        HashTable<String, String> table = new HashTable<>();
        table.put("Hello", "World");
        assertEquals(1, table.size());
        table.put("System out", "println");
        assertEquals(2, table.size());
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

    }

    @Test
    void get() {
    }

    @Test
    void put() {
    }

    @Test
    void remove() {
    }

    @Test
    void putAll() {
    }

    @Test
    void clear() {
    }

    @Test
    void keySet() {
    }

    @Test
    void values() {
    }

    @Test
    void entrySet() {
    }
}