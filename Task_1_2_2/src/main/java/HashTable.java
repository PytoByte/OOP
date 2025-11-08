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
         * {@inheritDoc}
         */
        @Override
        public K getKey() {
            return key;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public V getValue() {
            return value;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public V setValue(V value) {
            V old = this.value;
            this.value = value;
            return old;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Entry<?, ?> entry = (Entry<?, ?>) o;
            K key = getKey();
            V value = getValue();
            return (key == null ? entry.getKey() == null : key.equals(entry.getKey()))
                    && (value == null ? entry.getValue()==null : value.equals(entry.getValue()));
        }

        @Override
        public int hashCode() {
            return (getKey() == null ? 0 : getKey().hashCode())
                    ^ (getValue() == null ? 0 : getValue().hashCode());
        }
    }

    private ArrayList<LinkedList<Entry<K, V>>> table;
    private int size;
    private static final float LOAD_FACTOR = 0.75f;
    private int threshold;
    private int modCount = 0;

    /**
     * Constructs a new, empty hash table with the default initial capacity (16).
     */
    public HashTable() {
        this(16);
    }

    /**
     * Constructs a new, empty hash table with the specified initial capacity.
     *
     * @param initialCapacity the initial capacity of the hash table
     * @throws IllegalArgumentException if the initial capacity is less than or equal to 0
     */
    public HashTable(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive.");
        }
        this.table = new ArrayList<>(initialCapacity);
        for (int i = 0; i < initialCapacity; i++) {
            table.add(new LinkedList<>());
        }
        this.size = 0;
        this.threshold = (int) (initialCapacity * LOAD_FACTOR);
    }

    /**
     * Get index of bucket list in table.
     *
     * @param key key for value
     * @return index of bucket list in table
     */
    private int getIndex(Object key) {
        int hash = key != null ? key.hashCode() : 0;
        return Math.abs(hash) % table.size();
    }

    /**
     * Resizes the internal table when the number of elements exceeds the threshold.
     * Doubles the capacity and rehashes all existing entries.
     */
    private void resize() {
        ArrayList<LinkedList<Entry<K, V>>> oldTable = table;
        int oldCapacity = oldTable.size();
        int newCapacity = oldCapacity * 2;
        ArrayList<LinkedList<Entry<K, V>>> newTable = new ArrayList<>(newCapacity);
        for (int i = 0; i < newCapacity; i++) {
            newTable.add(new LinkedList<>());
        }

        for (LinkedList<Entry<K, V>> bucket : oldTable) {
            for (Entry<K, V> entry : bucket) {
                int newIndex =
                        Math.abs(entry.key != null ? entry.key.hashCode() : 0) % newCapacity;
                newTable.get(newIndex).add(entry);
            }
        }
        this.table = newTable;
        this.threshold = (int) (newCapacity * LOAD_FACTOR);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    @Override
    public V put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("null key");
        }

        if (size >= threshold) {
            resize();
        }

        int index = getIndex(key);
        LinkedList<Entry<K, V>> bucket = table.get(index);

        for (Entry<K, V> entry : bucket) {
            if (Objects.equals(entry.key, key)) {
                V oldValue = entry.value;
                entry.value = value;
                modCount++;
                return oldValue;
            }
        }

        bucket.add(new Entry<>(key, value));
        size++;
        modCount++;
        return null;
    }

    /**
     * {@inheritDoc}
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
                modCount++;
                size--;
                return entry.value;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        for (LinkedList<Entry<K, V>> bucket : table) {
            bucket.clear();
        }
        modCount++;
        size = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();
        for (LinkedList<Entry<K, V>> bucket : table) {
            for (Entry<K, V> pair : bucket) {
                keys.add(pair.getKey());
            }
        }
        return keys;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<V> values() {
        Set<V> values = new HashSet<>();
        for (LinkedList<Entry<K, V>> bucket : table) {
            for (Entry<K, V> pair : bucket) {
                values.add(pair.getValue());
            }
        }
        return values;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return new EntrySet();
    }

    private class EntrySet extends AbstractSet<Map.Entry<K, V>> {
        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new HashTableIterator();
        }

        @Override
        public int size() {
            return HashTable.this.size();
        }
    }

    private class HashTableIterator implements Iterator<Map.Entry<K, V>> {
        private final int expectedModCount = modCount;
        private final Iterator<LinkedList<Entry<K, V>>> bucketIterator = table.iterator();
        private Iterator<Entry<K, V>> currentBucketIterator = null;

        public HashTableIterator() {
            nextBucket();
        }

        private void nextBucket() {
            while (bucketIterator.hasNext()) {
                currentBucketIterator = bucketIterator.next().iterator();
                if (currentBucketIterator.hasNext()) {
                    return;
                }
            }
            currentBucketIterator = null;
        }

        @Override
        public boolean hasNext() {
            checkForComodification();

            if (currentBucketIterator == null) {
                nextBucket();
            } else if (!currentBucketIterator.hasNext()) {
                nextBucket();
            } else {
                return true;
            }

            return currentBucketIterator != null;
        }

        @Override
        public Map.Entry<K, V> next() {
            checkForComodification();

            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            return currentBucketIterator.next();
        }

        final void checkForComodification() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("{");
        Iterator<Map.Entry<K, V>> it = entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<K, V> entry = it.next();
            sb.append(entry.getKey().toString());
            sb.append(":");
            sb.append(entry.getValue().toString());

            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append("}");

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HashTable<?, ?> that = (HashTable<?, ?>) o;
        if (size() != that.size()) {
            return false;
        }

        for (Map.Entry<K, V> entry : entrySet()) {
            K key = entry.getKey();
            V value = entry.getValue();
            if (!Objects.equals(value, that.get(key))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        for (Map.Entry<K, V> entry : entrySet()) {
            result += entry.hashCode();
        }
        return result;
    }
}