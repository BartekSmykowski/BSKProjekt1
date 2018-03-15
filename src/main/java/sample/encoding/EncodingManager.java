package sample.encoding;

import sample.model.EncodingData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class EncodingManager {

    private EncodingData encodingData;
    private Encoder encoder;
    private Path sourceFilePath;
    private Path destinationFilePath;

    public EncodingManager(EncodingData encodingData){
        this.encodingData = encodingData;
        encoder = EncoderFactory.produce(
                encodingData.getEncodingMode(),
                encodingData.getSessionKey(),
                encodingData.getInitialVector());

        sourceFilePath = encodingData.getSelectedFile().toPath();
        destinationFilePath = Paths.get(encodingData.getSaveDirectory().getAbsolutePath() + "\\" + encodingData.getSaveFileName());
    }

    public void performEncoding(){
        byte[] data = tryLoadFileData(sourceFilePath);
        byte[] encoded = encoder.encode(data);
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




}
