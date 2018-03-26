package sample.model.ManagersData;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import sample.ciphering.cipherers.AES.AESCipherer;
import sample.ciphering.cipherers.AES.ECBAESCipherer;
import sample.ciphering.hashing.SHA256Hasher;
import sample.ciphering.encodedFileHeader.EncodedFileHeader;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class DataConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ManagerData fromEncodeToManager(EncodingData encodingData){
        ManagerData managerData = new ManagerData();
        managerData.setDestinationPath(
                Paths.get(
                        encodingData.getSaveDirectory().getAbsolutePath() + "\\" + encodingData.getSaveFileName()
                        )
                );
        managerData.setSourcePath(encodingData.getSelectedFile().toPath());
        managerData.setMode(encodingData.getEncodingMode());
        managerData.setSessionKey(encodingData.getSessionKey());
        managerData.setInitialVector(encodingData.getInitialVector());
        return managerData;
    }

    public static ManagerData fromDecodingToManager(DecodingData decodingData){
        ManagerData managerData = new ManagerData();

        Path sourcePath = decodingData.getSelectedFile().toPath();
        managerData.setSourcePath(sourcePath);

        EncodedFileHeader encodedFileHeader = getEncodedFileHeader(sourcePath);

        managerData.setMode(encodedFileHeader.getMode());



        byte[] sessionKey = new byte[0];
        try {
            String password = decodingData.getPassword();
            String sessionKeyBase64 = encodedFileHeader.getUsersKeys().get(decodingData.getSelectedUser().getLogin());
            byte[] sessionKeyEncoded = Base64.decode(sessionKeyBase64);

            byte[] hashedPasswordBytes = new SHA256Hasher().hash(password.getBytes());

            AESCipherer cipherer = new ECBAESCipherer(hashedPasswordBytes);
            byte[] encodedPrivateKey = cipherer.encode(decodingData.getSelectedUser().getEncodedPrivateRsaKey());

            KeyFactory kf = KeyFactory.getInstance("AES");
            PrivateKey privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(encodedPrivateKey));

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);

            sessionKey = cipher.doFinal(sessionKeyEncoded);

        } catch (Base64DecodingException | NoSuchPaddingException | InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }

        managerData.setSessionKey(sessionKey);
        managerData.setInitialVector(encodedFileHeader.getInitialVector());

        Path destinationPath = Paths.get(
                decodingData.getSaveDirectory().getAbsolutePath() + "\\" + decodingData.getSaveFileName() + "." + encodedFileHeader.getExtension()
        );
        managerData.setDestinationPath(destinationPath);

        return managerData;
    }

    private static EncodedFileHeader getEncodedFileHeader(Path sourcePath) {
        EncodedFileHeader encodedFileHeader = null;
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(sourcePath.toFile()))){
            String lineWithJsonSize = bufferedReader.readLine();
            int numberOfJsonLines = Integer.parseInt(lineWithJsonSize);
            StringBuilder stringBuilder = new StringBuilder();
            for(int i = 0; i < numberOfJsonLines; i++){
                stringBuilder.append(bufferedReader.readLine());
            }
            encodedFileHeader = objectMapper.readValue(stringBuilder.toString(), EncodedFileHeader.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return encodedFileHeader;
    }

}




