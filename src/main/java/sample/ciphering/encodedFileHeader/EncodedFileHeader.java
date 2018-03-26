package sample.ciphering.encodedFileHeader;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import lombok.Data;
import lombok.NoArgsConstructor;
import sample.model.EncodingModes;
import sample.model.ManagersData.EncodingData;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class EncodedFileHeader {
    private EncodingModes mode;
    private byte[] initialVector;
    private Map<String, String> usersKeys;
    private String extension;

    public EncodedFileHeader(EncodingData encodingData){
        this.mode = encodingData.getEncodingMode();
        this.initialVector = encodingData.getInitialVector();

        Map<String, String> usersWithBase64SessionKeys = new HashMap<>();
        for(Map.Entry<String, byte[]> userKey : encodingData.getAllowedUsersWithSessionKeys().entrySet()){
            usersWithBase64SessionKeys.put(userKey.getKey(), Base64.encode(userKey.getValue()));
        }
        this.usersKeys = usersWithBase64SessionKeys;
        this.extension = getFileExtension(encodingData.getSaveFileName());
    }

    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }

}
