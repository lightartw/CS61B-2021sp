package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }
    /* Instance Variables */
    private Collection<Node>[] buckets;
    private int initialSize;
    private int size;
    private double loadFactor;
    private HashSet<K> keySet;

    public MyHashMap() {
        this.initialSize = 16;
        this.size = 0;
        this.loadFactor = 0.75;
        buckets = createTable(initialSize);

        for (int i = 0; i < initialSize; i++) {
            buckets[i] = createBucket();
        }
    }

    public MyHashMap(int initialSize) {
        this.initialSize = initialSize;
        this.size = 0;
        this.loadFactor = 0.75;
        buckets = createTable(initialSize);

        for (int i = 0; i < initialSize; i++) {
            buckets[i] = createBucket();
        }
    }

    public MyHashMap(int initialSize, double maxLoad) {
        this.initialSize = initialSize;
        this.size = 0;
        this.loadFactor = maxLoad;
        buckets = createTable(initialSize);

        for (int i = 0; i < initialSize; i++) {
            buckets[i] = createBucket();
        }
    }

    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    protected Collection<Node> createBucket() {
        return new ArrayList<Node>();
    }

    private Collection<Node>[] createTable(int tableSize) {
        return new Collection[tableSize];
    }

    @Override
    public void clear(){
        for (int i = 0; i < this.initialSize; i++) {
            buckets[i] = null;
        }
        this.size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        if (key == null) return false;
        int hash = Math.floorMod(key.hashCode(), initialSize);
        Collection<Node> bucket = buckets[hash];
        if (bucket == null) return false;

        Iterator<Node> it = buckets[hash].iterator();
        while (it.hasNext()) {
            Node node = it.next();
            if (node.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public V get(K key){
        if (key == null) return null;

        int hash = Math.floorMod(key.hashCode(), initialSize);
        Collection<Node> bucket = buckets[hash];
        if (bucket == null) return null;

        Iterator<Node> it = buckets[hash].iterator();
        while (it.hasNext()) {
            Node node = it.next();
            if (node.key.equals(key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return this.size;
    }

    private void resize(int newSize) {
        Collection<Node>[] newBuckets = createTable(newSize);
        for (int i = 0; i < newSize; i++) {
            newBuckets[i] = createBucket();
        }

        for (Collection<Node> bucket : buckets) {
            if (bucket == null) continue;
            Iterator<Node> it = bucket.iterator();
            while (it.hasNext()) {
                Node node = it.next();
                int hash = Math.floorMod(node.key.hashCode(), newSize);
                newBuckets[hash].add(node);
            }
        }

        this.buckets = newBuckets;
        this.initialSize = newSize;
    }

    @Override
    public void put(K key, V value){
        if (key == null) return;

        if ((double) this.size / this.initialSize > loadFactor) {
            resize(this.initialSize * 2);
        }

        int hash = Math.floorMod(key.hashCode(), initialSize);
        Collection<Node> bucket = buckets[hash];

        Iterator<Node> it = buckets[hash].iterator();
        while (it.hasNext()) {
            Node node = it.next();
            if (node.key.equals(key)) {
                node.value = value;
                return;
            }
        }

        buckets[hash].add(createNode(key, value));
        size++;
    }

    @Override
    public Set<K> keySet(){
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key){
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value){
        throw new UnsupportedOperationException();
    }
}
