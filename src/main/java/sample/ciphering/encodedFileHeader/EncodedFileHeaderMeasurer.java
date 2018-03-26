package sample.ciphering.encodedFileHeader;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EncodedFileHeaderMeasurer {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private EncodedFileHeader fileHeader;

    public EncodedFileHeaderMeasurer(EncodedFileHeader fileHeader){

        this.fileHeader = fileHeader;
    }

    public int getJsonByteSize(){
        int size = -1;
        try {
            String headerJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(fileHeader);
            size = headerJson.getBytes().length;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return size;
    }

}
