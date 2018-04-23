package sample.ciphering.jobs;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
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
    private int outBlockLength;

    private long bytesToSkip;

    public DecodeJob(AESCipherer cipherer, String sourceFile, String destinationFile, int blockLength, long headerBytesToSkip) {
        this.cipherer = cipherer;
        this.sourceFile = sourceFile;
        this.destinationPath = destinationFile;
        this.outBlockLength = (blockLength/16 + 1)*16;
        this.bytesToSkip = headerBytesToSkip;
    }

    @Override
    public Void call() throws Exception {
        long fileSize = Files.size(Paths.get(sourceFile));
        long numberOfIterations = fileSize/outBlockLength;
        try(FileInputStream inputStream = new FileInputStream(sourceFile);
            FileOutputStream outputStream = new FileOutputStream(destinationPath)) {
            inputStream.skip(bytesToSkip);
            byte[] dataBlock = new byte[outBlockLength];
            int i = 1;
            while (inputStream.read(dataBlock) != -1) {
                byte[] decodedBlock;
                decodedBlock = decodeBlock(dataBlock);
                outputStream.write(decodedBlock);
                i++;
                progress.setValue((float)i/numberOfIterations);
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
