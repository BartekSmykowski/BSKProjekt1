package sample.ciphering.cipherManagers;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import sample.ciphering.cipherers.AES.AESCipherer;
import sample.ciphering.cipherers.AES.AESCiphererFactory;
import sample.ciphering.cipherers.AES.ECBAESCipherer;
import sample.ciphering.cipherers.KeyTypes;
import sample.ciphering.cipherers.RSACipherer;
import sample.ciphering.encodedFileHeader.EncodedFileHeader;
import sample.ciphering.encodedFileHeader.FileHeaderReader;
import sample.ciphering.hashing.SHA256Hasher;
import sample.ciphering.jobs.CipherJobExecutor;
import sample.ciphering.jobs.CiphererJob;
import sample.ciphering.jobs.JobFactory;
import sample.model.CipherModes;
import sample.model.ManagersData.DecodingData;

public class DecodeManager implements CipherManager {

    private CipherJobExecutor jobExecutor;

    public DecodeManager(DecodingData decodingData) {
        FileHeaderReader headerReader = new FileHeaderReader(decodingData.getSelectedFilePath());
        EncodedFileHeader fileHeader = headerReader.readHeader();

        byte[] sessionKey = getSessionKey(decodingData, fileHeader);

        AESCipherer cipherer = AESCiphererFactory.produce(fileHeader.getMode(), sessionKey, fileHeader.getInitialVector());

        String sourceFilePath = decodingData.getSelectedFilePath();
        String destinationFilePath = decodingData.getDestinationFilePath() + "." + fileHeader.getExtension();

        CiphererJob job = JobFactory.produce(cipherer, sourceFilePath, destinationFilePath, CipherModes.DECODE);

        jobExecutor = new CipherJobExecutor(job);
    }

    private byte[] getSessionKey(DecodingData decodingData, EncodedFileHeader fileHeader) {
        byte[] sessionKey = null;
        try {
            String sessionKeyBase64 = fileHeader.getUsersKeys().get(decodingData.getSelectedUser().getLogin());

            String password = decodingData.getPassword();
            byte[] sessionKeyEncoded = Base64.decode(sessionKeyBase64);

            byte[] hashedPassword = new SHA256Hasher().hash(password.getBytes());

            AESCipherer cipherer = new ECBAESCipherer(hashedPassword);
            byte[] decodedPrivateKey = cipherer.decode(decodingData.getSelectedUser().getEncodedPrivateRsaKey());

            RSACipherer rsaCipherer = new RSACipherer(KeyTypes.PRIVATE, decodedPrivateKey);

            sessionKey = rsaCipherer.decode(sessionKeyEncoded);
        }catch (Base64DecodingException e) {
            e.printStackTrace();
        }
        return sessionKey;
    }

    @Override
    public void performJob(){
        jobExecutor.execute();
    }

    @Override
    public CipherJobExecutor getJobExecutor() {
        return jobExecutor;
    }
}
