import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        List<Callable<ParallelRunner.TestResult>> tests = List.of(
                ParallelRunner.fakeTest("Test1", 350, ParallelRunner.Status.PASSED),
                ParallelRunner.fakeTest("Test2", 500, ParallelRunner.Status.FAILED),
                ParallelRunner.fakeTest("Test3", 440, ParallelRunner.Status.FAILED),
                ParallelRunner.fakeTest("Test4", 410, ParallelRunner.Status.PASSED),
                ParallelRunner.fakeTest("Test5", 390, ParallelRunner.Status.FAILED),
                ParallelRunner.fakeTest("Test6", 450, ParallelRunner.Status.PASSED),
                ParallelRunner.fakeTest("Test7", 421, ParallelRunner.Status.FAILED),
                ParallelRunner.fakeTest("Test8", 310, ParallelRunner.Status.PASSED)
        );

        ParallelRunner.runTests(tests);

    }
}