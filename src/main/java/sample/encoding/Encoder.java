package sample.encoding;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Encoder {
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Callable<Object> task;
    private Future future;

    public Encoder(EncoderJob task){
        this.task = task;
    }

    public void startEncoding() {
        future = executorService.submit(task);
    }
}
