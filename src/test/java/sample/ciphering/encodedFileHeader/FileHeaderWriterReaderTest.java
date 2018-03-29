package sample.ciphering.encodedFileHeader;

import org.junit.jupiter.api.Test;
import sample.Settings;
import sample.ciphering.key.generation.InitialVectorGenerator;
import sample.model.EncodingModes;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileHeaderWriterReaderTest {

    @Test
    void writingAndReadingHeader_shouldReturnProperHeader(){
        EncodedFileHeader encodedFileHeader = new EncodedFileHeader();
        encodedFileHeader.setExtension("asd");
        encodedFileHeader.setMode(EncodingModes.CBC);
        InitialVectorGenerator initialVectorGenerator = new InitialVectorGenerator(Settings.INITIAL_VECTOR_SIZE);
        encodedFileHeader.setInitialVector(initialVectorGenerator.generate());
        Map<String, byte[]> usersKeys = new HashMap<>();
        byte[] sessionKey = new byte[] {1,2,3,4,5};
        usersKeys.put("qwe", sessionKey);
        usersKeys.put("asd", sessionKey);
        encodedFileHeader.setUsersKeys(usersKeys);

        String path = "testFiles/test.txt";
        FileHeaderWriter writer = new FileHeaderWriter(path);
        writer.write(encodedFileHeader);

        FileHeaderReader reader = new FileHeaderReader(path);
        EncodedFileHeader actual = reader.readHeader();

        assertEquals(encodedFileHeader, actual);

    }

}