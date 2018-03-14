package sample.encoding;

import sample.model.EncodingData;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class ECBEncoderJob extends EncoderJob {

    public ECBEncoderJob(EncodingData encodingData) {
        super(encodingData);
    }

    @Override
    public void doStep() {
        encode();
    }

    private void encode(){
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(encodingData.getSessionKey(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            Path filePath = encodingData.getSelectedFile().toPath();
            byte[] fileBytes = Files.readAllBytes(filePath);
            byte[] encodedFileBytes = cipher.doFinal(fileBytes);

            Path path = Paths.get(encodingData.getSaveDirectory().getAbsolutePath() + "\\" + encodingData.getSaveFileName());
            Files.write(path, encodedFileBytes);

//            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
//
//            byte[] original = cipher.doFinal(encodedFileBytes);
//            Path savePath = Paths.get(encodingData.getSaveDirectory().getAbsolutePath() + "\\decrypted" + encodingData.getSaveFileName());
//            Files.write(savePath, original);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | BadPaddingException
                | IllegalBlockSizeException | InvalidKeyException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getNumberOfIterations() {
        return 1;
    }
}
