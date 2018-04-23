package sample.ciphering.cipherManagers;

import sample.ciphering.cipherers.AES.AESCipherer;
import sample.ciphering.cipherers.AES.AESCiphererFactory;
import sample.ciphering.cipherers.AES.ECBAESCipherer;
import sample.ciphering.cipherers.KeyTypes;
import sample.ciphering.cipherers.RSACipherer;
import sample.ciphering.encodedFileHeader.EncodedFileHeader;
import sample.ciphering.encodedFileHeader.EncodedFileHeaderMeasurer;
import sample.ciphering.encodedFileHeader.FileHeaderReader;
import sample.ciphering.hashing.SHA256Hasher;
import sample.ciphering.jobs.CipherJobExecutor;
import sample.ciphering.jobs.CiphererJob;
import sample.ciphering.jobs.DecodeJob;
import sample.ciphering.jobs.MockJob;
import sample.model.ManagersData.DecodingData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DecodeManager implements CipherManager {

    private CipherJobExecutor jobExecutor;
    private String sourceFilePath;
    private String destinationFilePath;

    public DecodeManager(DecodingData decodingData) {
        FileHeaderReader headerReader = new FileHeaderReader(decodingData.getSelectedFilePath());
        EncodedFileHeader fileHeader = headerReader.readHeader();

        byte[] sessionKey = getSessionKey(decodingData, fileHeader);

        sourceFilePath = decodingData.getSelectedFilePath();
        destinationFilePath = decodingData.getDestinationFilePath() + "." + fileHeader.getExtension();

        EncodedFileHeaderMeasurer measurer = new EncodedFileHeaderMeasurer(fileHeader);
        long headerBytesSize = measurer.getJsonByteSize();

        CiphererJob job;
        if(sessionKey.length == 0){
            job = new MockJob(sourceFilePath, destinationFilePath, headerBytesSize);
        } else{
            AESCipherer cipherer = AESCiphererFactory.produce(fileHeader.getMode(), sessionKey, fileHeader.getInitialVector());
            job = new DecodeJob(cipherer, sourceFilePath, destinationFilePath, fileHeader.getBlockLength(), headerBytesSize);
        }

        jobExecutor = new CipherJobExecutor(job);
    }

    private byte[] getSessionKey(DecodingData decodingData, EncodedFileHeader fileHeader) {
        byte[] sessionKeyEncoded = fileHeader.getUsersKeys().get(decodingData.getSelectedUser().getLogin());

        String password = decodingData.getPassword();

        byte[] hashedPassword = new SHA256Hasher().hash(password.getBytes());

        AESCipherer cipherer = new ECBAESCipherer(hashedPassword);
        byte[] decodedPrivateKey = cipherer.decode(decodingData.getSelectedUser().getEncodedPrivateRsaKey());

        RSACipherer rsaCipherer = new RSACipherer(KeyTypes.PRIVATE, decodedPrivateKey);

        return rsaCipherer.decode(sessionKeyEncoded);
    }

    @Override
    public void performJob(){
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
    public float getFileSize() {
        try {
            return (Files.size(Paths.get(sourceFilePath))/(float)1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
