package sample.ciphering.cipherManagers;

import sample.ciphering.cipherers.AES.AESCipherer;
import sample.ciphering.cipherers.AES.AESCiphererFactory;
import sample.ciphering.encodedFileHeader.EncodedFileHeader;
import sample.ciphering.encodedFileHeader.FileHeaderWriter;
import sample.ciphering.jobs.CipherJobExecutor;
import sample.ciphering.jobs.CiphererJob;
import sample.ciphering.jobs.EncodeJob;
import sample.model.EncodingModes;
import sample.model.ManagersData.EncodingData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class EncodeManager implements CipherManager {

    private CipherJobExecutor jobExecutor;
    private EncodingData encodingData;
    private String destinationFilePath;
    private String sourceFilePath;

    public EncodeManager(EncodingData encodingData){
        this.encodingData = encodingData;
        CiphererJob job = createCiphererJob(encodingData);

        jobExecutor = new CipherJobExecutor(job);
    }

    private CiphererJob createCiphererJob(EncodingData encodingData) {
        AESCipherer cipherer = createCipherer(encodingData);
        sourceFilePath = encodingData.getSelectedFile();
        destinationFilePath = encodingData.getDestinationFilePath();
        return new EncodeJob(cipherer, sourceFilePath, destinationFilePath);
    }

    private AESCipherer createCipherer(EncodingData encodingData) {
        EncodingModes mode = encodingData.getEncodingMode();
        byte[] sessionKey = encodingData.getSessionKey();
        byte[] initialVector = encodingData.getInitialVector();
        return AESCiphererFactory.produce(mode, sessionKey, initialVector);
    }

    @Override
    public void performJob(){
        EncodedFileHeader fileHeader = new EncodedFileHeader(encodingData);
        FileHeaderWriter headerWriter = new FileHeaderWriter(destinationFilePath);
        headerWriter.write(fileHeader);
        fileHeader.print();
        jobExecutor.execute();
    }

    @Override
    public CipherJobExecutor getJobExecutor() {
        return jobExecutor;
    }
    @Override
    public String getSourcePath() {
        return sourceFilePath;
    }

    @Override
    public String getDestinationPath() {
        return destinationFilePath;
    }

    @Override
    public int getFileSize() {
        try {
            return (int) (Files.size(Paths.get(sourceFilePath))/1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
