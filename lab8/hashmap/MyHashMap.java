package hashmap;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

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
    private int size;
    private double loadFactor;

    public MyHashMap() {
        this.size = 16;
        this.loadFactor = 0.75;
        buckets = new Collection[size];
    }

    public MyHashMap(int initialSize) {
        this.size = initialSize;
        this.loadFactor = 0.75;
        buckets = new Collection[size];
    }

    public MyHashMap(int initialSize, double maxLoad) {
        this.size = initialSize;
        this.loadFactor = maxLoad;
        buckets = new Collection[size];
    }

    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    protected Collection<Node> createBucket() {
        return null;
    }

    private Collection<Node>[] createTable(int tableSize) {
        return new Collection[tableSize];
    }

    @Override
    public void clear(){
        this.size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V get(K key){
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return this.size;
    }


    @Override
    public void put(K key, V value){
        throw new UnsupportedOperationException();
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
