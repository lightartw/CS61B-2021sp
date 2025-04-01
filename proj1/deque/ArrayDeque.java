package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>{
    private T[] items;
    private int size;
    private int front;
    private int back;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        front = 0;
        back = 0;
    }

    private void resize(int capacity){
        T[] a = (T[])new Object[capacity];
        if (front < back)
            System.arraycopy(items, front, a, 0, size);
        else {
            int interval = items.length - front;
            System.arraycopy(items, front, a, 0, interval);
            System.arraycopy(items, 0, a, interval, back);
        }
        items = a;
        front = 0;
        back = size;
    }

    @Override
    public void addFirst(T i){
        if (size == items.length) {
            resize(items.length * 2); // 扩容
        }
        front = (front - 1 + items.length) % items.length;
        items[front] = i;
        size++;
    }

    @Override
    public void addLast(T i){
        if (size == items.length) {
            resize(items.length * 2); // 扩容
        }
        items[back] = i;
        back = (back + 1) % items.length;
        size++;
    }

    @Override
    public int size(){
        return size;
    }

    @Override
    public void printDeque(){
        for (int i = 0; i < size; i++) {
            System.out.print(items[(front + i) % items.length] + " ");
        }
        System.out.println();
    }

    @Override
    public T removeFirst(){
        if (size == 0) return null;

        T item = items[front];
        front = (front + 1) % items.length;
        size--;

        if (items.length >= 16 && size > 0 && size == items.length / 4)
            resize(items.length / 2);
        return item;
    }

    @Override
    public T removeLast(){
        if (size == 0) return null;

        back = (back - 1 + items.length) % items.length;
        T item = items[back];
        size--;

        if (items.length >= 16 && size > 0 && size == items.length / 4)
            resize(items.length / 2);
        return item;
    }

    @Override
    public T get(int index){
        if(index < 0 || index >= size) return null;

        int i = (front + index) % items.length;
        return items[i];
    }

    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<T>{
        int pos;
        public ArrayDequeIterator(){pos = 0;}
        public boolean hasNext(){return pos < size;}

        public T next(){
            if(pos < size){
                return items[pos++];
            }
            return null;
        }
    }

    public boolean equals(Object o){
        if(o == this) return true;
        if(o == null) return false;
        if(!(o instanceof ArrayDeque)) return false;
        ArrayDeque<T> other = (ArrayDeque<T>) o;
        if(size != other.size) return false;
        for(int i = 0; i < size; i++){
            if(!items[i].equals(other.items[i])) return false;
        }
        return true;
    }
}
