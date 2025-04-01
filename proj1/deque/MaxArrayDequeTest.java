package deque;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Comparator;

public class MaxArrayDequeTest{
    @Test
    public void test() {
        Comparator<Integer> c = Integer::compare;
        MaxArrayDeque<Integer> list = new MaxArrayDeque<>(c);
        list.addFirst(1);
        list.addFirst(2);
        list.addFirst(3);
        list.addFirst(0);
        int maxVal = list.max();
        assertEquals(3, maxVal);
    }
}
