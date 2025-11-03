import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

/**
 * Hash table realisation with generic keys and values.
 *
 * @param <K> key type
 * @param <V> value type
 */
public class HashTable<K, V> implements Map<K, V> {
    /**
     * Pair of key and value.
     *
     * @param <K> key type
     * @param <V> value type
     */
    private static class Entry<K, V> implements Map.Entry<K, V> {
        private final K key;
        private V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        /**
         * @inheritDoc
         */
        @Override
        public K getKey() {
            return key;
        }

        /**
         * @inheritDoc
         */
        @Override
        public V getValue() {
            return value;
        }

        /**
         * @inheritDoc
         */
        @Override
        public V setValue(V value) {
            V old = this.value;
            this.value = value;
            return old;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Entry<?, ?> entry = (Entry<?, ?>) o;
            return Objects.equals(key, entry.key) && Objects.equals(value, entry.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }
    }

    private ArrayList<LinkedList<Entry<K, V>>> table;
    private int size;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private final float loadFactor;
    private int threshold;

    /**
     * Constructs a new, empty hash table with the default initial capacity (16)
     * and the default load factor (0.75).
     */
    public HashTable() {
        this(16, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Constructs a new, empty hash table with the specified initial capacity
     * and the default load factor (0.75).
     *
     * @param initialCapacity the initial capacity of the hash table
     * @throws IllegalArgumentException if the initial capacity is less than or equal to 0
     */
    public HashTable(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Constructs a new, empty hash table with the specified initial capacity
     * and the specified load factor.
     *
     * @param initialCapacity the initial capacity of the hash table
     * @param loadFactor the load factor threshold for resizing the table
     * @throws IllegalArgumentException if the initial capacity is less than or equal to 0
     */
    public HashTable(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive.");
        }
        this.loadFactor = loadFactor;
        this.table = new ArrayList<>(initialCapacity);
        for (int i = 0; i < initialCapacity; i++) {
            table.add(new LinkedList<>());
        }
        this.size = 0;
        this.threshold = (int) (initialCapacity * loadFactor);
    }

    /**
     * Get index of bucket list in table.
     *
     * @param key key for value
     * @return index of bucket list in table
     */
    private int getIndex(Object key) {
        int hash = key != null ? key.hashCode() : 0;
        // Use bitwise operation for faster modulo on power-of-two sizes
        return Math.abs(hash) & (table.size() - 1);
    }

    /**
     * Resizes the internal table when the number of elements exceeds the threshold.
     * Doubles the capacity and rehashes all existing entries.
     */
    private void resize() {
        ArrayList<LinkedList<Entry<K, V>>> oldTable = table;
        int oldCapacity = oldTable.size();
        int newCapacity = oldCapacity << 1; // Equivalent to oldCapacity * 2
        ArrayList<LinkedList<Entry<K, V>>> newTable = new ArrayList<>(newCapacity);
        for (int i = 0; i < newCapacity; i++) {
            newTable.add(new LinkedList<>());
        }

        for (LinkedList<Entry<K, V>> bucket : oldTable) {
            for (Entry<K, V> entry : bucket) {
                int newIndex = Math.abs(entry.key != null ? entry.key.hashCode() : 0) & (newCapacity - 1);
                newTable.get(newIndex).add(entry);
            }
        }
        this.table = newTable;
        this.threshold = (int) (newCapacity * loadFactor);
    }

    /**
     * @inheritDoc
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean containsKey(Object key) {
        int index = getIndex(key);
        LinkedList<Entry<K, V>> bucket = table.get(index);
        for (Entry<K, V> entry : bucket) {
            if (Objects.equals(entry.key, key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean containsValue(Object value) {
        for (LinkedList<Entry<K, V>> bucket : table) {
            for (Entry<K, V> entry : bucket) {
                if (Objects.equals(entry.value, value)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @inheritDoc
     */
    @Override
    public V get(Object key) {
        int index = getIndex(key);
        LinkedList<Entry<K, V>> bucket = table.get(index);
        for (Entry<K, V> entry : bucket) {
            if (Objects.equals(entry.key, key)) {
                return entry.value;
            }
        }
        return null;
    }

    /**
     * @inheritDoc
     */
    @Override
    public V put(K key, V value) {
        if (size >= threshold) {
            resize();
        }

        int index = getIndex(key);
        LinkedList<Entry<K, V>> bucket = table.get(index);

        for (Entry<K, V> entry : bucket) {
            if (Objects.equals(entry.key, key)) {
                V oldValue = entry.value;
                entry.value = value;
                return oldValue;
            }
        }

        bucket.add(new Entry<>(key, value));
        size++;
        return null;
    }

    /**
     * @inheritDoc
     */
    @Override
    public V remove(Object key) {
        int index = getIndex(key);
        LinkedList<Entry<K, V>> bucket = table.get(index);

        Iterator<Entry<K, V>> it = bucket.iterator();
        while (it.hasNext()) {
            Entry<K, V> entry = it.next();
            if (Objects.equals(entry.key, key)) {
                it.remove();
                size--;
                return entry.value;
            }
        }
        return null;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public void clear() {
        for (LinkedList<Entry<K, V>> bucket : table) {
            bucket.clear();
        }
        size = 0;
    }

    private transient Set<K> keySet = null;
    private transient Collection<V> values = null;

    /**
     * @inheritDoc
     */
    @Override
    public Set<K> keySet() {
        if (keySet == null) {
            keySet = new AbstractSet<K>() {
                @Override
                public Iterator<K> iterator() {
                    return new KeyIterator();
                }

                @Override
                public int size() {
                    return HashTable.this.size();
                }

                @Override
                public boolean contains(Object o) {
                    return HashTable.this.containsKey(o);
                }

                @Override
                public boolean remove(Object o) {
                    return HashTable.this.remove(o) != null;
                }

                @Override
                public void clear() {
                    HashTable.this.clear();
                }
            };
        }
        return keySet;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<V> values() {
        if (values == null) {
            values = new AbstractCollection<V>() {
                @Override
                public Iterator<V> iterator() {
                    return new ValueIterator();
                }

                @Override
                public int size() {
                    return HashTable.this.size();
                }

                @Override
                public boolean contains(Object o) {
                    return HashTable.this.containsValue(o);
                }

                @Override
                public void clear() {
                    HashTable.this.clear();
                }
            };
        }
        return values;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> entries = new HashSet<>();
        for (LinkedList<Entry<K, V>> bucket : table) {
            entries.addAll(bucket);
        }
        return entries;
    }

    /**
     * An iterator over the entries of the hash table.
     * Supports removal of the last returned element.
     */
    private class TableIterator implements Iterator<Entry<K, V>> {
        private int currentIndex = 0;
        private Iterator<Entry<K, V>> currentBucketIterator = null;
        private Entry<K, V> lastReturned = null;

        /**
         * Advances the iterator to the next available entry.
         */
        private void advanceToNext() {
            if (currentBucketIterator != null && currentBucketIterator.hasNext()) {
                return;
            }

            while (currentIndex < table.size()) {
                currentBucketIterator = table.get(currentIndex).iterator();
                if (currentBucketIterator.hasNext()) {
                    break;
                }
                currentIndex++;
            }
        }

        @Override
        public boolean hasNext() {
            advanceToNext();
            return currentBucketIterator != null && currentBucketIterator.hasNext();
        }

        @Override
        public Entry<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            lastReturned = currentBucketIterator.next();
            return lastReturned;
        }

        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new IllegalStateException();
            }

            int index = getIndex(lastReturned.key);
            LinkedList<Entry<K, V>> bucket = table.get(index);
            Iterator<Entry<K, V>> it = bucket.iterator();
            while (it.hasNext()) {
                Entry<K, V> e = it.next();
                if (e == lastReturned) {
                    it.remove();
                    size--;
                    lastReturned = null;
                    return;
                }
            }

            throw new ConcurrentModificationException();
        }
    }


    /**
     * An iterator over the keys of the hash table.
     * Delegates iteration to the internal TableIterator.
     */
    private class KeyIterator implements Iterator<K> {
        private final TableIterator tableIterator = new TableIterator();

        @Override
        public boolean hasNext() {
            return tableIterator.hasNext();
        }

        @Override
        public K next() {
            return tableIterator.next().getKey();
        }

        @Override
        public void remove() {
            tableIterator.remove();
        }
    }

    /**
     * An iterator over the values of the hash table.
     * Delegates iteration to the internal TableIterator.
     */
    private class ValueIterator implements Iterator<V> {
        private final TableIterator tableIterator = new TableIterator();

        @Override
        public boolean hasNext() {
            return tableIterator.hasNext();
        }

        @Override
        public V next() {
            return tableIterator.next().getValue();
        }

        @Override
        public void remove() {
            tableIterator.remove();
        }
    }
}