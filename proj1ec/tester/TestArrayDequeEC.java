package tester;

import static org.junit.Assert.*;

import edu.princeton.cs.introcs.StdRandom;
import org.junit.Test;
import student.StudentArrayDeque;

public class TestArrayDequeEC {
    StudentArrayDeque<Integer> s1 = new StudentArrayDeque<>();
    ArrayDequeSolution<Integer> s2 = new ArrayDequeSolution<>();
    @Test
    public void test1() {

        for (int i = 0; i < 500; i++) {
            double random = StdRandom.uniform();
            if (random < 0.5) {
                s1.addLast(i);
                s2.addLast(i);
                assertEquals("addLast" + "(" + i + ")",i, i);
            } else {
                s1.addFirst(i);
                s2.addFirst(i);
                assertEquals("addFirst" + "(" + i + ")",i, i);
            }
        }

        for (int i = 0; i < 500; i++) {
            double random = StdRandom.uniform();
            Integer res1, res2;
            if (random < 0.5) {
                res1 = s1.removeFirst();
                res2 = s2.removeFirst();
                assertEquals("removeFirst():", res2, res1);
            }
            else{
                res1 = s1.removeLast();
                res2 = s2.removeLast();
                assertEquals("removeLast():"+res2, res2, res1);
            }
        }
    }
}
