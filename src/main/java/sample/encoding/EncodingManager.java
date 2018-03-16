package sample.encoding;

import javafx.beans.property.DoubleProperty;
import sample.encoding.encoders.Encoder;
import sample.encoding.encoders.EncoderFactory;
import sample.model.EncodingData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class EncodingManager {

    private EncodingData encodingData;
    private Encoder encoder;
    private Path sourceFilePath;
    private Path destinationFilePath;
    private EncodeJobExecutor jobExecutor;

    public EncodingManager(EncodingData encodingData){
        this.encodingData = encodingData;
        encoder = EncoderFactory.produce(
                encodingData.getEncodingMode(),
                encodingData.getSessionKey(),
                encodingData.getInitialVector());

        sourceFilePath = encodingData.getSelectedFile().toPath();
        destinationFilePath = Paths.get(encodingData.getSaveDirectory().getAbsolutePath() + "\\" + encodingData.getSaveFileName());

        EncodingJob encodingJob = new EncodingJob(encoder, tryLoadFileData(sourceFilePath), encodingData.getCipherModes());
        jobExecutor = new EncodeJobExecutor(encodingJob);
    }

    public void performEncoding(){
        Future<byte[]> future = jobExecutor.execute();
        byte[] encoded = new byte[0];
        try {
            encoded = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        trySaveDataToFile(encoded, destinationFilePath);
    }

    public byte[] tryLoadFileData(Path path){
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void trySaveDataToFile(byte[] data, Path path){
        try {
            Files.write(path, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DoubleProperty progressProperty(){
        return encoder.progressProperty();
    }

}
