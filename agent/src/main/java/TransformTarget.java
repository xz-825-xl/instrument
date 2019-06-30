/**
 * Created by Mmn on 2019/6/29.
 * To be a happy coder!
 */
public class TransformTarget {

    public static void main(String[] args) {
        while (true) {
            try {
                Thread.sleep(3000L);
            } catch (Exception e) {
                break;
            }
            printSomething();
        }
    }

    public static void printSomething() {
        System.out.println("hello");
    }

}
