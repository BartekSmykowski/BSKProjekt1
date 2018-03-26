package sample.model.ManagersData;

import lombok.Data;
import sample.model.User;

@Data
public class DecodingData {

    private String selectedFilePath;
    private String saveDirectoryPath;
    private User selectedUser;
    private String saveFileName;
    private String password;

    public DecodingData(){

    }

    public String getDestinationFilePath(){
        return saveDirectoryPath + "/" + saveFileName;
    }

    public boolean isValid(){
        return selectedFilePath != null && saveDirectoryPath != null
                && saveFileName != null && selectedUser != null
                && password != null;
    }
}
