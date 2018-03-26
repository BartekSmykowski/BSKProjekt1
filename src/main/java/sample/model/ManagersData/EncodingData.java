package sample.model.ManagersData;

import lombok.Data;
import sample.model.EncodingModes;

import java.io.File;
import java.util.Map;

@Data
public class EncodingData {

    private File selectedFile;
    private File saveDirectory;
    private EncodingModes encodingMode;
    private Map<String, byte[]> allowedUsersWithSessionKeys;
    private byte[] sessionKey;
    private byte[] initialVector;
    private String saveFileName;

    public EncodingData(){

    }

    public boolean isValid(){
        return selectedFile != null && saveDirectory != null
                && encodingMode != null && sessionKey != null
                && saveFileName != null && initialVector != null
                && allowedUsersWithSessionKeys != null;
    }

}
