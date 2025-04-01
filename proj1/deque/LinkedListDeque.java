package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T> {
    private Node sentinel;
    private int size;

    private class Node {
        public Node prev;
        public T item;
        public Node next;

        public Node(Node pre, T ite, Node nex) {
            prev = pre;
            item = ite;
            next = nex;
        }
    }

    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    public LinkedListDeque(T i) {
        sentinel = new Node(null, null, null);
        sentinel.next = new Node(sentinel,i,sentinel);
        sentinel.prev = sentinel.next;
    }

    @Override
    public void addFirst(T i) {
        size += 1;
        Node second = sentinel.next;
        sentinel.next = new Node(sentinel,i,second);
        second.prev = sentinel.next;
    }

    @Override
    public void addLast(T i) {
        size += 1;
        Node last = sentinel.prev;
        sentinel.prev = new Node(last,i,sentinel);
        last.next = sentinel.prev;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        Node cur = sentinel.next;
        while(cur.item != null) {
            System.out.print(cur.item + " ");
            cur = cur.next;
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if(this.isEmpty()) {
            return null;
        }

        size -= 1;
        T res = sentinel.next.item;
        sentinel.next.next.prev = sentinel;
        sentinel.next = sentinel.next.next;
        return res;
    }

    @Override
    public T removeLast() {
        if(this.isEmpty()) {
            return null;
        }

        size -= 1;
        T res = sentinel.prev.item;
        sentinel.prev.prev.next = sentinel;
        sentinel.prev = sentinel.prev.prev;
        return res;
    }

    @Override
    public T get(int index) {
        if(this.isEmpty()) {
            return null;
        }

        Node cur = sentinel.next;
        for(int i = 0; i < index; i++) {
            cur = cur.next;
        }
        return cur.item;
    }

    private T getR(int index, Node cur) {
        if(this.isEmpty()) {
            return null;
        }

        if(index == 0) {
            return cur.item;
        }
        return getR(index - 1, cur.next);
    }

    public T getRecursive(int index) {
        Node cur = sentinel.next;
        return getR(index, cur);
    }

    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }

    private class LinkedListDequeIterator implements Iterator<T> {
        private int pos;

        public LinkedListDequeIterator() {
            pos = 0;
        }

        public boolean hasNext() {
            return pos < size;
        }

        public T next() {
            T res = get(pos);
            pos++;
            return res;
        }
    }

    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(o == null) {
            return false;
        }
        if(!(o instanceof Deque)) {
            return false;
        }
        Deque<T> other = (Deque<T>) o;
        if(size != other.size()) {
            return false;
        }
        for(int i = 0; i < size; i++) {
            if(!get(i).equals(other.get(i))) {
                return false;
            }
        }
        return true;
    }

}
