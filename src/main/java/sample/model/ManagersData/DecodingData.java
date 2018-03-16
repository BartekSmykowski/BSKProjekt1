package sample.model.ManagersData;

import lombok.Data;
import sample.model.User;

import java.io.File;

@Data
public class DecodingData {

    private File selectedFile;
    private File saveDirectory;
    private User selectedUser;
    private String saveFileName;
    private String password;

    public DecodingData(){

    }

    public boolean isValid(){
        return selectedFile != null && saveDirectory != null
                && saveFileName != null && selectedUser != null
                && password != null;
    }
}
