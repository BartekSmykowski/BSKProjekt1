package sample.ciphering;

import sample.ciphering.jobs.Job;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CipherJobExecutor {
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Job task;

    public CipherJobExecutor(Job task){
        this.task = task;
    }

    public Future<byte[]> execute() {
        return executorService.submit(task);
    }

}
