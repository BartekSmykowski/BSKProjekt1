package sample.ciphering.jobs;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import sample.Settings;
import sample.ciphering.cipherers.AES.AESCipherer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DecodeJob implements CiphererJob {

    private DoubleProperty progress = new SimpleDoubleProperty();
    protected AESCipherer cipherer;
    private String sourceFile;
    private String destinationPath;

    private long bytesToSkip;

    public DecodeJob(AESCipherer cipherer, String sourceFile, String destinationFile, long headerBytesToSkip) {
        this.cipherer = cipherer;
        this.sourceFile = sourceFile;
        this.destinationPath = destinationFile;
        this.bytesToSkip = headerBytesToSkip;
    }

    @Override
    public Void call() throws Exception {
        long fileSize = Files.size(Paths.get(sourceFile));
        long numberOfIterations = fileSize/272;
        try(FileInputStream inputStream = new FileInputStream(sourceFile);
            FileOutputStream outputStream = new FileOutputStream(destinationPath)) {
            inputStream.skip(bytesToSkip);
            byte[] dataBlock = new byte[Settings.OUT_PACKET_SIZE];
            int i = 1;
            while (inputStream.read(dataBlock) != -1) {
                byte[] decodedBlock = decodeBlock(dataBlock);
                outputStream.write(decodedBlock);
                i++;
                progress.setValue(i/numberOfIterations);
            }
        }
        progress.setValue(1.0);
        return null;
    }

    private byte[] decodeBlock(byte[] dataBlock) {
        return cipherer.decode(dataBlock);
    }

    @Override
    public DoubleProperty progressProperty() {
        return progress;
    }
}
