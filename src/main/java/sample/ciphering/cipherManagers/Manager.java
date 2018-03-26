package sample.ciphering.cipherManagers;

import javafx.beans.property.DoubleProperty;
import sample.ciphering.CipherJobExecutor;
import sample.ciphering.cipherers.AES.AESCipherer;
import sample.ciphering.cipherers.AES.AESCiphererFactory;
import sample.ciphering.jobs.Job;
import sample.ciphering.jobs.JobFactory;
import sample.model.CipherModes;
import sample.model.ManagersData.ManagerData;

import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public abstract class Manager {

    private AESCipherer cipherer;
    private Path sourceFilePath;
    private Path destinationFilePath;
    private CipherJobExecutor jobExecutor;
    protected ManagerData managerData;

    public Manager(ManagerData managerData, CipherModes cipherMode){
        this.managerData = managerData;
        cipherer = AESCiphererFactory.produce(managerData.getMode(), managerData.getSessionKey(), managerData.getInitialVector());

        sourceFilePath = managerData.getSourcePath();
        destinationFilePath = managerData.getDestinationPath();

        Job job = JobFactory.produce(cipherer, loadFileData(sourceFilePath), cipherMode);

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
        return cipherer.progressProperty();
    }

}
