package bstmap;

import org.w3c.dom.Node;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private int size;
    private Node root;

    private class Node {
        K key;
        V value;
        Node left;
        Node right;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public BSTMap() {
        this.size = 0;
        this.root = null;
    }

    public void printInOrder() {
    }

    @Override
    public void clear(){
       this.root = null;
       this.size = 0;
    }

    private boolean nodeContains(Node node, K key) {
        if (node == null) {
            return false;
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return nodeContains(node.left, key);
        } else if (cmp > 0) {
            return nodeContains(node.right, key);
        } else {
            return true;
        }
    }

    @Override
    public boolean containsKey(K key) {
        if (this.root == null) {
            return false;
        }
        return nodeContains(this.root, key);
    }

    private V nodeGet(Node node, K key) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return nodeGet(node.left, key);
        }
        else if (cmp > 0) {
            return nodeGet(node.right, key);
        }
        else {
            return node.value;
        }
    }

    @Override
    public V get(K key){
        return nodeGet(this.root, key);
    }

    @Override
    public int size() {
        return this.size;
    }

    private Node nodePut(Node node, K key, V value) {
        if (node == null) {
            size++;
            return new Node(key, value);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = nodePut(node.left, key, value);
        }
        else if (cmp > 0) {
            node.right = nodePut(node.right, key, value);
        }
        else {
            node.value = value;
        }
        return node;
    }

    @Override
    public void put(K key, V value){
        this.root = nodePut(this.root, key, value);
    }

    @Override
    public Set<K> keySet(){
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

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }
}
