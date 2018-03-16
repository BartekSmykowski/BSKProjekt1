package sample.encoding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class EncodeJobExecutor {
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private EncodingJob task;

    public EncodeJobExecutor(EncodingJob task){
        this.task = task;
    }

    public Future<byte[]> execute() {
        return executorService.submit(task);
    }
}
