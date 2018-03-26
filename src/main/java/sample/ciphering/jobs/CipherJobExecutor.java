package sample.ciphering.jobs;

import javafx.beans.property.DoubleProperty;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CipherJobExecutor {
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private CiphererJob task;

    public CipherJobExecutor(CiphererJob task){
        this.task = task;
    }

    public void execute() {
        executorService.submit(task);
    }

    public DoubleProperty progressProperty(){
        return task.progressProperty();
    }

}
