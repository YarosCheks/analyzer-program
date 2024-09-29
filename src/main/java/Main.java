import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {

    private static final BlockingQueue<String> queueA = new ArrayBlockingQueue<>(100);
    private static final BlockingQueue<String> queueB = new ArrayBlockingQueue<>(100);
    private static final BlockingQueue<String> queueC = new ArrayBlockingQueue<>(100);

    private static int maxNumA = 0;
    private static int maxNumB = 0;
    private static int maxNumC = 0;

    public static void main(String[] args) throws InterruptedException {
        
        new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                String text = generateText("abc", 10_000);
                try {
                    queueA.put(text);
                    queueB.put(text);
                    queueC.put(text);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        Thread threadA = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                int numA;
                try {
                    numA = numberCharacters(queueA.take(), 'a');
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (maxNumA < numA) {
                    maxNumA = numA;
                }
            }
        });
        threadA.start();

        Thread threadB = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                int numB;
                try {
                    numB = numberCharacters(queueB.take(), 'b');
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (maxNumB < numB) {
                    maxNumB = numB;
                }
            }
        });
        threadB.start();

        Thread threadC = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                int numC;
                try {
                    numC = numberCharacters(queueC.take(), 'c');
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (maxNumC < numC) {
                    maxNumC = numC;
                }
            }
        });
        threadC.start();

        threadB.join();
        threadB.join();
        threadC.join();

        System.out.println("Максимальное a = " + maxNumA);
        System.out.println("Максимальное b = " + maxNumB);
        System.out.println("Максимальное c = " + maxNumC);
    }

    private static int numberCharacters(String text, char target) {
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == target) {
                count++;
            }
        }
        return count;
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}
