package sample.ciphering.jobs;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import sample.ciphering.cipherers.AES.AESCipherer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.concurrent.Callable;

public abstract class CiphererJob implements Callable<Void> {

    private DoubleProperty progress = new SimpleDoubleProperty();
    protected AESCipherer cipherer;
    private String sourceFile;
    private String destinationPath;
    protected boolean appendToOutput;

    public CiphererJob(AESCipherer cipherer, String sourceFile, String destinationFile){
        this.cipherer = cipherer;
        this.sourceFile = sourceFile;
        this.destinationPath = destinationFile;
    }

    @Override
    public Void call() throws Exception {
        try(FileInputStream inputStream = new FileInputStream(sourceFile);
            FileOutputStream outputStream = new FileOutputStream(destinationPath, appendToOutput)) {
            byte[] dataBlock = new byte[256];
            while (inputStream.read(dataBlock) != -1) {
                outputStream.write(perform(dataBlock));
            }
        }
        progress.setValue(1.0);
        return null;
    }

    protected abstract byte[] perform(byte[] dataBlock);

    public double getProgress() {
        return progress.get();
    }

    public DoubleProperty progressProperty() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress.set(progress);
    }
}
