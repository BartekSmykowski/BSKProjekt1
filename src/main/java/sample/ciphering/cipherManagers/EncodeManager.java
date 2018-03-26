package sample.ciphering.cipherManagers;

import sample.ciphering.cipherers.AES.AESCipherer;
import sample.ciphering.cipherers.AES.AESCiphererFactory;
import sample.ciphering.encodedFileHeader.EncodedFileHeader;
import sample.ciphering.encodedFileHeader.FileHeaderWriter;
import sample.ciphering.jobs.CipherJobExecutor;
import sample.ciphering.jobs.CiphererJob;
import sample.ciphering.jobs.JobFactory;
import sample.model.CipherModes;
import sample.model.EncodingModes;
import sample.model.ManagersData.EncodingData;

public class EncodeManager implements CipherManager {

    private CipherJobExecutor jobExecutor;
    private EncodingData encodingData;
    private String destinationFilePath;

    public EncodeManager(EncodingData encodingData){
        this.encodingData = encodingData;
        CiphererJob job = createCiphererJob(encodingData);

        jobExecutor = new CipherJobExecutor(job);

    }

    private CiphererJob createCiphererJob(EncodingData encodingData) {
        AESCipherer cipherer = createCipherer(encodingData);
        String sourceFilePath = encodingData.getSelectedFile();
        destinationFilePath = encodingData.getDestinationFilePath();

        return JobFactory.produce(cipherer, sourceFilePath, destinationFilePath, CipherModes.ENCODE);
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
        jobExecutor.execute();
    }

    @Override
    public CipherJobExecutor getJobExecutor() {
        return jobExecutor;
    }

}
