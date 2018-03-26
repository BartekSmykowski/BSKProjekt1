package sample.model.ManagersData;

import lombok.Data;
import sample.model.EncodingModes;

import java.util.Map;

@Data
public class EncodingData {

    private String selectedFile;
    private String saveDirectory;
    private EncodingModes encodingMode;
    private Map<String, byte[]> allowedUsersWithSessionKeys;
    private byte[] sessionKey;
    private byte[] initialVector;
    private String saveFileName;

    public EncodingData(){

    }

    public String getDestinationFilePath(){
        return saveDirectory + "/" + saveFileName;
    }

    public boolean isValid(){
        return selectedFile != null && saveDirectory != null
                && encodingMode != null && sessionKey != null
                && saveFileName != null && initialVector != null
                && allowedUsersWithSessionKeys != null;
    }

}
