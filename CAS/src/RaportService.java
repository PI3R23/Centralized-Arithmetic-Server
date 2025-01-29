import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.CopyOnWriteArrayList;

public class RaportService {
    private static final AtomicInteger newClients = new AtomicInteger(0);
    private static final AtomicInteger tmpNewClients = new AtomicInteger(0);
    private static final AtomicInteger calculatedOperations = new AtomicInteger(0);
    private static final AtomicInteger tmpCalculatedOperations = new AtomicInteger(0);
    private static final AtomicInteger operations = new AtomicInteger(0);
    private static final AtomicInteger tmpOperations = new AtomicInteger(0);
    private static final AtomicInteger wrongOperations = new AtomicInteger(0);
    private static final AtomicInteger tmpWrongOperations = new AtomicInteger(0);
    private static final CopyOnWriteArrayList<Integer> sum = new CopyOnWriteArrayList<>();
    private static final CopyOnWriteArrayList<Integer> tmpSum = new CopyOnWriteArrayList<>();

    public static void getTmpStatistics() {
        new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(10000);

                    synchronized (RaportService.class) {
                        System.out.println("-------------------------------------------");
                        System.out.println("Server statistics (last 10 seconds):");
                        System.out.println("Number of newly connected clients: " + tmpNewClients.get());
                        System.out.println("Number of calculated operations: " + tmpCalculatedOperations.get());
                        System.out.println("Number of individual operations: " + tmpOperations.get());
                        System.out.println("Number of incorrect operations: " + tmpWrongOperations.get());
                        System.out.println("Sum of results: " + tmpSum.stream().mapToInt(Integer::intValue).sum());
                        System.out.println("-------------------------------------------");

                        tmpNewClients.set(0);
                        tmpCalculatedOperations.set(0);
                        tmpOperations.set(0);
                        tmpWrongOperations.set(0);
                        tmpSum.clear();
                    }
                }
            } catch (InterruptedException e) {
                System.err.println("Error printing last 10 seconds statistics: "+e.getMessage());
            }
        }).start();
    }

    public static void getStatistics() {
        new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(10000);

                    synchronized (RaportService.class) {
                        System.out.println("-------------------------------------------");
                        System.out.println("Global Server Statistics:");
                        System.out.println("Total clients: " + newClients.get());
                        System.out.println("Total calculated operations: " + calculatedOperations.get());
                        System.out.println("Total operations: " + operations.get());
                        System.out.println("Total wrong operations: " + wrongOperations.get());
                        System.out.println("Sum of results: " + sum.stream().mapToInt(Integer::intValue).sum());
                        System.out.println("-------------------------------------------");
                    }
                }
            } catch (InterruptedException e) {
                System.err.println("Error printing general statistics: "+e.getMessage());
            }
        }).start();
    }

    public static void incrementNewClient() {
        tmpNewClients.incrementAndGet();
        newClients.incrementAndGet();
    }

    public static void incrementCalculatedOperation() {
        tmpCalculatedOperations.incrementAndGet();
        calculatedOperations.incrementAndGet();
    }

    public static void incrementOperation() {
        tmpOperations.incrementAndGet();
        operations.incrementAndGet();
    }

    public static void incrementWrongOperation() {
        tmpWrongOperations.incrementAndGet();
        wrongOperations.incrementAndGet();
    }

    public static void addSumResult(int result) {
        tmpSum.add(result);
        sum.add(result);
    }
}
