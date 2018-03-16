package sample.encoding.cipherManagers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import sample.exception.CannotSaveUsersException;
import sample.model.CipherModes;
import sample.model.EncodedFileHeader;
import sample.model.ManagersData.DataConverter;
import sample.model.ManagersData.EncodingData;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
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
            Files.readAllBytes(path);
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

        Map<String, String> usersKeysMap = new HashMap<>();
        encodingData.getAllowedUsers().forEach(user->usersKeysMap.put(user.getLogin(), user.getEncodedPrivateRsaKey()));
        fileHeader.setUsersKeys(usersKeysMap);

        ObjectMapper objectMapper = new ObjectMapper();

        try(PrintWriter writer = new PrintWriter(path.toFile()))
        {
            writer.print(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(fileHeader));
        } catch (FileNotFoundException | JsonProcessingException e) {
            throw new CannotSaveUsersException(e.getMessage());
        }

        try {
            Files.write(path, data, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
