package sample.encoding.cipherManagers;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import sample.model.CipherModes;
import sample.model.ManagersData.DataConverter;
import sample.model.ManagersData.DecodingData;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class DecodeManager extends Manager {

    public DecodeManager(DecodingData decodingData) {
        super(DataConverter.fromDecodingToManager(decodingData), CipherModes.DECRYPT);
    }

    @Override
    protected byte[] loadFileData(Path path) {
        byte[] data = new byte[0];
        try(BufferedReader bufferedReader = new BufferedReader(
                                                new InputStreamReader(
                                                    new FileInputStream(path.toFile())))){
            String lineWithJsonSize = bufferedReader.readLine();
            int numberOfJsonLines = Integer.parseInt(lineWithJsonSize);
            for(int i = 0; i < numberOfJsonLines; i++){
                bufferedReader.readLine();
            }
            String line;
            //StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                data = Base64.decode(line);
            }
            //data = stringBuilder.toString().getBytes();
        }catch (Exception e){
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected void saveDataToFile(byte[] data, Path path) {
        try {
            Files.write(path, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
