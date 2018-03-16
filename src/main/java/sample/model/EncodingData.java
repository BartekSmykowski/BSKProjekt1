package sample.model;

import lombok.Data;

import java.io.File;
import java.util.List;

@Data
public class EncodingData {

    private File selectedFile;
    private File saveDirectory;
    private EncodingModes encodingMode;
    private List<User> allowedUsers;
    private byte[] sessionKey;
    private byte[] initialVector;
    private String saveFileName;
    private CipherModes cipherModes;

    public EncodingData(){

    }

    public boolean isValid(){
        return selectedFile != null && saveDirectory != null
                && encodingMode != null && sessionKey != null
                && saveFileName != null && initialVector != null
                && allowedUsers != null;
    }

}
