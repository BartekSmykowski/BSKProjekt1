package sample.model.ManagersData;

import lombok.Data;
import sample.model.EncodingModes;

import java.nio.file.Path;

@Data
public class ManagerData {

    private EncodingModes mode;
    private byte[] sessionKey;
    private byte[] initialVector;
    private Path sourcePath;
    private Path destinationPath;

}
