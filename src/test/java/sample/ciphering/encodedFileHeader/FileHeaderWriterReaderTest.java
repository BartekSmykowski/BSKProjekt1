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
        Map<String, String> usersKeys = new HashMap<>();
        usersKeys.put("qwe", "qwe");
        encodedFileHeader.setUsersKeys(usersKeys);

        String path = "testFiles/test.txt";
        FileHeaderWriter writer = new FileHeaderWriter(path);
        writer.write(encodedFileHeader);

        FileHeaderReader reader = new FileHeaderReader(path);
        EncodedFileHeader actual = reader.readHeader();

        assertEquals(encodedFileHeader, actual);

    }

}