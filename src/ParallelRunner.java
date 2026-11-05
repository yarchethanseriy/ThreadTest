import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ParallelRunner {

    public enum Status {
        PASSED,
        FAILED
    }

    public record TestResult(String name, Status status, long durationMs) {}

    static Callable<TestResult> fakeTest(String name, long durationMs, Status status) {
        return () -> {
            Thread.sleep(durationMs);
            return new TestResult(name, status, durationMs);
        };
    }

    public static void runTests(List<Callable<TestResult>> tests)  {
        try (ExecutorService executor = Executors.newFixedThreadPool(4)) {
            long startTime = System.nanoTime();
            List<Future<TestResult>> results = executor.invokeAll(tests);
            List<TestResult> resultsOfTests = new ArrayList<>();

            for (Future<TestResult> result : results) {
                resultsOfTests.add(result.get());
            }
            long endTime = System.nanoTime();

            int passed = 0;
            int failed = 0;

            for (TestResult result : resultsOfTests) {
                if (result.status() == Status.PASSED) {
                    passed++;
                } else if (result.status() == Status.FAILED) {
                    failed++;
                }
            }

            long actualTime =  (endTime - startTime) /  1_000_000;
            long totalTime = 0;
            for (TestResult result : resultsOfTests) {
                totalTime += result.durationMs();
            }
            long savedTime = totalTime - actualTime;

            System.out.println("Прошло: " + passed + ", упало: " + failed);
            System.out.println("Суммарное время тестов: " + totalTime + " ms");
            System.out.println("Реальное время прогона: " + actualTime + " ms");
            System.out.println("Параллельность сэкономила: " + savedTime + " ms");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

}
