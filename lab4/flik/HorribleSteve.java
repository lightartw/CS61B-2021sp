package flik;

public class HorribleSteve {
    public static void main(String [] args) throws Exception {
        int j = 0;
        for (int i = 0; i < 500; ++i, ++j) {
            if (!Flik.isSameNumber(i, j)) {
                throw new Exception(
                        String.format("i:%d not same as j:%d ??", i, j));
            }
        }
        System.out.println("i is " + j);
    }
}
