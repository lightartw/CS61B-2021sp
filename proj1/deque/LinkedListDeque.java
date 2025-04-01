package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Iterable<T> {
    private node sentinel;
    private int size;

    public class node{
        public node prev;
        public T item;
        public node next;

        public node(node pre, T ite, node nex){
            prev = pre;
            item = ite;
            next = nex;
        }
    }

    public LinkedListDeque(){
        sentinel = new node(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    public LinkedListDeque(T i){
        sentinel = new node(null, null, null);
        sentinel.next = new node(sentinel,i,sentinel);
        sentinel.prev = sentinel.next;
    }

    public void addFirst(T i){
        size += 1;
        node second = sentinel.next;
        sentinel.next = new node(sentinel,i,second);
        second.prev = sentinel.next;
    }

    public void addLast(T i){
        size += 1;
        node last = sentinel.prev;
        sentinel.prev = new node(last,i,sentinel);
        last.next = sentinel.prev;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public int size(){return size;}

    public void printDeque(){
        node cur = sentinel.next;
        while(cur.item != null){
            System.out.print(cur.item + " ");
            cur = cur.next;
        }
        System.out.println();
    }

    public T removeFirst(){
        if(this.isEmpty()) return null;

        size -= 1;
        T res = sentinel.next.item;
        sentinel.next.next.prev = sentinel;
        sentinel.next = sentinel.next.next;
        return res;
    }

    public T removeLast(){
        if(this.isEmpty()) return null;

        size -= 1;
        T res = sentinel.prev.item;
        sentinel.prev.prev.next = sentinel;
        sentinel.prev = sentinel.prev.prev;
        return res;
    }

    public T get(int index){
        if(this.isEmpty()) return null;

        node cur = sentinel.next;
        for(int i = 0; i < index; i++){
            cur = cur.next;
        }
        return cur.item;
    }

    private T getR(int index, node cur){
        if(this.isEmpty()) return null;

        if(index == 0) return cur.item;
        return getR(index - 1, cur.next);
    }

    public T getRecursive(int index){
        node cur = sentinel.next;
        return getR(index, cur);
    }

    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }

    private class LinkedListDequeIterator implements Iterator<T>{
        private int pos;

        public LinkedListDequeIterator(){
            pos = 0;
        }

        public boolean hasNext(){return pos < size;}

        public T next(){
            T res = get(pos);
            pos++;
            return res;
        }
    }

    public boolean equals(Object o){
        if(o == this) return true;
        if(o == null) return false;
        if(!(o instanceof LinkedListDeque)){ return false; }
        LinkedListDeque<T> other = (LinkedListDeque) o;
        if(size != other.size) return false;
        for(int i = 0; i < size; i++){
            if(!get(i).equals(other.get(i))) return false;
        }
        return true;
    }

}
