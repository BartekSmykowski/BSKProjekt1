package sample.ciphering.encodedFileHeader;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import sample.exception.CannotSaveUsersException;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class FileHeaderWriter {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private String path;

    public FileHeaderWriter(String path){
        this.path = path;
    }

    public void write(EncodedFileHeader fileHeader){

        try(PrintWriter writer = new PrintWriter(path))
        {
            String headerJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(fileHeader);
            writer.print(headerJson);
        } catch (FileNotFoundException | JsonProcessingException e) {
            throw new CannotSaveUsersException(e.getMessage());
        }

    }

}
