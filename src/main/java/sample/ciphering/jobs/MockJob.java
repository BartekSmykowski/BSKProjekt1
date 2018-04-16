package sample.ciphering.jobs;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import sample.Settings;
import sample.ciphering.key.generation.RandomBytesGenerator;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MockJob implements CiphererJob {

    private DoubleProperty progress = new SimpleDoubleProperty();
    private String sourceFile;
    private String destinationPath;

    private long bytesToSkip;

    public MockJob(String sourceFile, String destinationFile, long headerBytesToSkip){
        this.sourceFile = sourceFile;
        this.destinationPath = destinationFile;
        this.bytesToSkip = headerBytesToSkip;
    }

    @Override
    public DoubleProperty progressProperty() {
        return progress;
    }

    @Override
    public Void call() throws Exception {
        long fileSize = Files.size(Paths.get(sourceFile));
        long numberOfIterations = fileSize/ Settings.OUT_PACKET_SIZE;
        try(FileInputStream inputStream = new FileInputStream(sourceFile);
            FileOutputStream outputStream = new FileOutputStream(destinationPath)) {
            inputStream.skip(bytesToSkip);
            byte[] dataBlock = new byte[Settings.OUT_PACKET_SIZE];
            int i = 1;
            while (inputStream.read(dataBlock) != -1) {
                byte[] decodedBlock;
                RandomBytesGenerator randomBytesGenerator = new RandomBytesGenerator(Settings.OUT_PACKET_SIZE);
                decodedBlock = randomBytesGenerator.generate();
                outputStream.write(decodedBlock);
                i++;
                progress.setValue((float)i/numberOfIterations);
            }
        }
        progress.setValue(1.0);
        return null;
    }
}
