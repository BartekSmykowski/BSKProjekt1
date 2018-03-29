package sample.ciphering.jobs;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import sample.Settings;
import sample.ciphering.cipherers.AES.AESCipherer;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class EncodeJob implements CiphererJob {

    private DoubleProperty progress = new SimpleDoubleProperty();
    protected AESCipherer cipherer;
    private String sourceFile;
    private String destinationPath;

    public EncodeJob(AESCipherer cipherer, String sourceFile, String destinationFile) {
        this.cipherer = cipherer;
        this.sourceFile = sourceFile;
        this.destinationPath = destinationFile;
    }

    @Override
    public Void call() throws Exception {
        try(FileInputStream inputStream = new FileInputStream(sourceFile);
            FileOutputStream outputStream = new FileOutputStream(destinationPath, true)) {
            byte[] dataBlock = new byte[Settings.DATA_PACKET_SIZE];
            while (inputStream.read(dataBlock) != -1) {
                byte[] encodedBlock = encodeBlock(dataBlock);
                outputStream.write(encodedBlock);
            }
        }
        progress.setValue(1.0);
        return null;
    }

    private byte[] encodeBlock(byte[] dataBlock) {
        return cipherer.encode(dataBlock);
    }

    @Override
    public DoubleProperty progressProperty() {
        return progress;
    }
}
