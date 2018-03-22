package sample.ciphering.cipherManagers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import sample.exception.CannotSaveUsersException;
import sample.model.CipherModes;
import sample.model.EncodedFileHeader;
import sample.model.ManagersData.DataConverter;
import sample.model.ManagersData.EncodingData;
import sample.model.User;
import sample.persistence.UsersLoader;
import sun.security.rsa.RSAPublicKeyImpl;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

public class EncodeManager extends Manager {
    private EncodingData encodingData;

    public EncodeManager(EncodingData encodingData){
        super(DataConverter.fromEncodeToManager(encodingData), CipherModes.ENCRYPT);
        this.encodingData = encodingData;
    }

    @Override
    protected byte[] loadFileData(Path path) {
        byte[] data = new byte[0];
        try {
            data = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected void saveDataToFile(byte[] data, Path path) {
        EncodedFileHeader fileHeader = new EncodedFileHeader();
        fileHeader.setMode(managerData.getMode());
        fileHeader.setInitialVector(managerData.getInitialVector());

        UsersLoader usersLoader = new UsersLoader();

        Map<String, User> usersMap = usersLoader.loadUsersMap();

        Map<String, String> allowedUsersKeys = encodingData.getAllowedUsers();

        Map<String, String> usersEncodedKeys = new HashMap<>();

        try {
            for(String login : allowedUsersKeys.keySet()){
                byte[] userPublicKey = usersMap.get(login).getPublicRsaKey();
                Cipher cipher = Cipher.getInstance("RSA");
                PublicKey publicRSA = new RSAPublicKeyImpl(userPublicKey);
                cipher.init(Cipher.ENCRYPT_MODE, publicRSA);
                String encodedByte64SessionKey = Base64.encode(cipher.doFinal(managerData.getSessionKey()));
                usersEncodedKeys.put(login, encodedByte64SessionKey);
            }
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }


        fileHeader.setUsersKeys(usersEncodedKeys);
        fileHeader.setExtension(getFileExtension(managerData.getSourcePath().toFile()));

        ObjectMapper objectMapper = new ObjectMapper();

        try(PrintWriter writer = new PrintWriter(path.toFile()))
        {
            String headerJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(fileHeader);
            writer.println(getLineCount(headerJson));
            writer.print(headerJson);
            writer.println();
            writer.print(Base64.encode(data));
        } catch (FileNotFoundException | JsonProcessingException e) {
            throw new CannotSaveUsersException(e.getMessage());
        }

//        try {
//            Files.write(path, data, StandardOpenOption.APPEND);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private static int getLineCount(String text){
        return text.split("[\n]").length;
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        try {
            return name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }

}
