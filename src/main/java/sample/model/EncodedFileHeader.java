package sample.model;

import lombok.Data;

import java.util.Map;

@Data
public class EncodedFileHeader {
    private EncodingModes mode;
    private byte[] initialVector;
    private Map<String, String> usersKeys;
    private String extension;
}
