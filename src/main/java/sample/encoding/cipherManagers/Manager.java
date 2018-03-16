package sample.encoding.cipherManagers;

import javafx.beans.property.DoubleProperty;
import sample.encoding.CipherJobExecutor;
import sample.encoding.encoders.Encoder;
import sample.encoding.encoders.EncoderFactory;
import sample.encoding.jobs.Job;
import sample.encoding.jobs.JobFactory;
import sample.model.CipherModes;
import sample.model.ManagersData.ManagerData;

import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public abstract class Manager {

    private Encoder encoder;
    private Path sourceFilePath;
    private Path destinationFilePath;
    private CipherJobExecutor jobExecutor;
    protected ManagerData managerData;

    public Manager(ManagerData managerData, CipherModes cipherMode){
        this.managerData = managerData;
        encoder = EncoderFactory.produce(managerData.getMode(), managerData.getSessionKey(), managerData.getInitialVector());

        sourceFilePath = managerData.getSourcePath();
        destinationFilePath = managerData.getDestinationPath();

        Job job = JobFactory.produce(encoder, loadFileData(sourceFilePath), cipherMode);

        jobExecutor = new CipherJobExecutor(job);
    }

    public void performJob(){
        Future<byte[]> future = jobExecutor.execute();
        byte[] data = new byte[0];
        try {
            data = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        saveDataToFile(data, destinationFilePath);
    }

    protected abstract byte[] loadFileData(Path path);

    protected abstract void saveDataToFile(byte[] data, Path path);

    public DoubleProperty progressProperty(){
        return encoder.progressProperty();
    }

}
