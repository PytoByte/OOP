import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class HashTable<K, V> implements Map<K, V> {
    private static class Entry<K, V> implements Map.Entry<K, V> {
        private K key;
        private V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V old = this.value;
            return null;
        }

        @Override
        public boolean equals(Object o) {
            return false;
        }

        @Override
        public int hashCode() {
            return 0;
        }
    }

    ArrayList<LinkedList<Entry<K, V>>> table = new ArrayList<>();

    private int getIndex(Object key) {
        int hash = key != null ? key.hashCode() : 0;
        return Math.abs(hash) % table.size();
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return table.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        for (LinkedList<Entry<K, V>> list : table) {
            for (Entry<K, V> entry : list) {
                if (entry.key.equals(key)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        for (LinkedList<Entry<K, V>> list : table) {
            for (Entry<K, V> entry : list) {
                if (entry.value.equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        int index = getIndex(key);

        LinkedList<Entry<K, V>> bucket = table.get(index);
        if (bucket == null) {
            return null;
        }

        for (Entry<K, V> entry : bucket) {
            if (Objects.equals(entry.key, key)) {
                return entry.value;
            }
        }

        return null;
    }

    @Override
    public V put(K key, V value) {
        int index = getIndex(key);

        LinkedList<Entry<K, V>> bucket = table.get(index);

        if (bucket == null) {
            bucket = new LinkedList<>();
            table.add(index, bucket);
        }

        for (Entry<K, V> entry : bucket) {
            if (Objects.equals(entry.key, key)) {
                V oldValue = entry.value;
                entry.value = value;
                return oldValue;
            }
        }

        bucket.add(new Entry<>(key, value));
        return null;
    }

    @Override
    public V remove(Object key) {
        int index = getIndex(key);
        LinkedList<Entry<K, V>> bucket = table.get(index);
        if (bucket == null) return null;

        Iterator<Entry<K, V>> it = bucket.iterator();
        while (it.hasNext()) {
            Entry<K, V> entry = it.next();
            if (Objects.equals(entry.key, key)) {
                it.remove();
                if (bucket.isEmpty()) {
                    table.set(index, null);
                }
                return entry.value;
            }
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        table.clear();
    }

    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();
        for (LinkedList<Entry<K, V>> bucket : table) {
            if (bucket != null) {
                for (Entry<K, V> entry : bucket) {
                    keys.add(entry.key);
                }
            }
        }
        return keys;
    }

    @Override
    public Collection<V> values() {
        List<V> values = new ArrayList<>();
        for (LinkedList<Entry<K, V>> bucket : table) {
            if (bucket != null) {
                for (Entry<K, V> entry : bucket) {
                    values.add(entry.value);
                }
            }
        }
        return values;
    }

    @Override
    public Set<Map.Entry<K,V>> entrySet() {
        Set<Map.Entry<K, V>> entries = new HashSet<>();
        for (LinkedList<Entry<K, V>> bucket : table) {
            if (bucket != null) {
                entries.addAll(bucket);
            }
        }
        return entries;
    }
}
