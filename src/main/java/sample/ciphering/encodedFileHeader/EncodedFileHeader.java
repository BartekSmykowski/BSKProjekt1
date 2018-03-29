package sample.ciphering.encodedFileHeader;

import lombok.Data;
import lombok.NoArgsConstructor;
import sample.ciphering.cipherers.KeyTypes;
import sample.ciphering.cipherers.RSACipherer;
import sample.model.EncodingModes;
import sample.model.ManagersData.EncodingData;
import sample.model.User;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class EncodedFileHeader {
    private EncodingModes mode;
    private byte[] initialVector;
    private Map<String, byte[]> usersKeys;
    private String extension;

    public EncodedFileHeader(EncodingData encodingData){
        this.mode = encodingData.getEncodingMode();
        this.initialVector = encodingData.getInitialVector();

        Map<String, byte[]> usersWithBase64SessionKeys = new HashMap<>();
        for(User user : encodingData.getAllowedUsers()){
            byte[] userPublicKey = user.getPublicRsaKey();

            RSACipherer cipherer = new RSACipherer(KeyTypes.PUBLIC, userPublicKey);

            byte[] encodedSessionKey = cipherer.encode(encodingData.getSessionKey());
            usersWithBase64SessionKeys.put(user.getLogin(), encodedSessionKey);
        }

        this.usersKeys = usersWithBase64SessionKeys;
        this.extension = getFileExtension(encodingData.getSelectedFile());
    }

    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }

    public void print(){
        System.out.println(this.getMode());
        System.out.println(this.getExtension());
        System.out.println(new String(this.getInitialVector()));
        for(Map.Entry<String, byte[]> user : this.getUsersKeys().entrySet()) {
            System.out.println(user.getKey());
            System.out.println(new String(user.getValue()));
        }
    }

    @Override public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof EncodedFileHeader)) return false;
        EncodedFileHeader other = (EncodedFileHeader) o;
        if (!this.mode.equals(other.mode)) return false;
        if (!Arrays.equals(this.initialVector, other.initialVector)) return false;
        if (!this.extension.equals(other.extension)) return false;
        for(Map.Entry<String, byte[]> entry : usersKeys.entrySet()) {
            if(!usersKeys.containsKey(entry.getKey())) return false;
            if (!Arrays.equals(this.usersKeys.get(entry.getKey()), other.usersKeys.get(entry.getKey()))) return false;
        }
        return true;
    }

    private boolean canEqual(Object other) {
        return other instanceof EncodedFileHeader;
    }

}
