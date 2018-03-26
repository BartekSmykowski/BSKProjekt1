package sample.ciphering.encodedFileHeader;

import lombok.Data;
import sample.model.EncodingModes;

import java.util.Map;

@Data
public class EncodedFileHeader {
    private EncodingModes mode;
    private byte[] initialVector;
    private Map<String, String> usersKeys;
    private String extension;

}
