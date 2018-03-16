package sample.encoding.cipherManagers;

import sample.model.CipherModes;
import sample.model.ManagersData.DataConverter;
import sample.model.ManagersData.DecodingData;

import java.nio.file.Path;

public class DecodeManager extends Manager {

    public DecodeManager(DecodingData decodingData) {
        super(DataConverter.fromDecodingToManager(decodingData), CipherModes.DECRYPT);
    }

    @Override
    protected byte[] loadFileData(Path path) {
        return new byte[0];
    }

    @Override
    protected void saveDataToFile(byte[] data, Path path) {

    }
}
