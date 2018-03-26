package sample.ciphering.encodedFileHeader;

import org.junit.jupiter.api.Test;
import sample.Settings;
import sample.ciphering.key.generation.InitialVectorGenerator;
import sample.model.EncodingModes;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EncodedFileHeaderTest {

    @Test
    void getJsonHeaderSize_shouldReturnProperResponse(){
        EncodedFileHeader encodedFileHeader = new EncodedFileHeader();
        encodedFileHeader.setExtension("asd");
        encodedFileHeader.setMode(EncodingModes.CBC);
        InitialVectorGenerator initialVectorGenerator = new InitialVectorGenerator(Settings.INITIAL_VECTOR_SIZE);
        encodedFileHeader.setInitialVector(initialVectorGenerator.generate());
        Map<String, String> usersKeys = new HashMap<>();
        usersKeys.put("qwe", "qwe");
        encodedFileHeader.setUsersKeys(usersKeys);

        EncodedFileHeaderMeasurer measurer = new EncodedFileHeaderMeasurer(encodedFileHeader);

        int expectedSize = 139;

        int size = measurer.getJsonByteSize();

        assertEquals(expectedSize, size);
    }



}