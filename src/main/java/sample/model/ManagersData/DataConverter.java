package sample.model.ManagersData;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import sample.model.EncodedFileHeader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;

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

        String sessionKey = encodedFileHeader.getUsersKeys().get(decodingData.getSelectedUser().getLogin());

        byte[] sessionKeyBytes = tryGetSessionKeyBytes(sessionKey);
        managerData.setSessionKey(sessionKeyBytes);

        managerData.setInitialVector(encodedFileHeader.getInitialVector());


        Path destinationPath = Paths.get(
                decodingData.getSaveDirectory().getAbsolutePath() + "\\" + decodingData.getSaveFileName() + "." + encodedFileHeader.getExtension()
        );
        managerData.setDestinationPath(destinationPath);

        return managerData;
    }

    private static byte[] tryGetSessionKeyBytes(String sessionKey) {
        byte[] sessionKeyBytes = new byte[0];
        try {
            sessionKeyBytes = Base64.decode(sessionKey);
        } catch (Base64DecodingException e) {
            e.printStackTrace();
        }
        return sessionKeyBytes;
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




