package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {

    private Comparator<T> comparator;

    public MaxArrayDeque(Comparator<T> c) {
        comparator = c;
    }

    public T max() {
        return max(comparator);
    }

    public T max(Comparator<T> c) {
        T res = null;
        for (int i = 0; i < size(); i++) {
            T t = get(i);
            int cmp = c.compare(res, t);
            if (cmp > 0) {
                res = t;
            }
        }
        return res;
    }
}
