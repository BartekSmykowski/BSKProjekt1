package sample.ciphering.jobs;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import sample.Settings;
import sample.ciphering.cipherers.AES.AESCipherer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

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
        long fileSize = Files.size(Paths.get(sourceFile));
        long numberOfIterations = fileSize/Settings.DATA_PACKET_SIZE;
        try(FileInputStream inputStream = new FileInputStream(sourceFile);
            FileOutputStream outputStream = new FileOutputStream(destinationPath, true)) {
            byte[] dataBlock = new byte[Settings.DATA_PACKET_SIZE];
            int i = 1;
            while (inputStream.read(dataBlock) != -1) {
                byte[] encodedBlock = encodeBlock(dataBlock);
                outputStream.write(encodedBlock);
                i++;
                progress.setValue((float)i/numberOfIterations);
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
