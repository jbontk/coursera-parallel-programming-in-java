import javax.swing.*;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class MainThread {

    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(1);

        SwingWorker<String, Object> worker = new SwingWorker<>() {

            @Override
            protected String doInBackground() throws Exception {
                latch.await();
                return "done";
            }

            @Override
            protected void done() {
                try {
                    get();
                } catch (InterruptedException e) {
                    System.err.println("InterruptedException caught " + e);
                } catch (ExecutionException e) {
                    System.err.println("ExecutionException caught " + e);
                } catch (CancellationException e) {
                    System.err.println("CancellationException caught " + e);
                }
            }
        };

        // start worker
        worker.execute();

        // cancel worker with mayInterruptIfRunning = true
        sleep();
        worker.cancel(true);
    }

    private static void sleep() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
