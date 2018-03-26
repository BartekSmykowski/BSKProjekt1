package sample.ciphering.encodedFileHeader;

import com.fasterxml.jackson.databind.ObjectMapper;
import sample.exception.FirstByteInFileIsNotOpeningBraceException;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class FileHeaderReader {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private String filePath;
    private final Character OPEN_BRACE = '{';
    private final Character CLOSE_BRACE = '}';

    public FileHeaderReader(String filePath){
        this.filePath = filePath;
    }

    public EncodedFileHeader readHeader(){

        int openedBlocks = 0;
        ByteArrayOutputStream headerBytes = new ByteArrayOutputStream();
        EncodedFileHeader encodedFileHeader = null;

        try(FileInputStream fileInputStream = new FileInputStream(filePath)){
            char character = (char) fileInputStream.read();
            if(character != OPEN_BRACE){
                throw new FirstByteInFileIsNotOpeningBraceException(character);
            } else {
                openedBlocks++;
                headerBytes.write(character);
            }
            while(openedBlocks > 0){
                int readByte = fileInputStream.read();
                headerBytes.write(readByte);
                character = (char) readByte;
                if(character == OPEN_BRACE){
                    openedBlocks++;
                } else if(character == CLOSE_BRACE){
                    openedBlocks--;
                }
            }
            encodedFileHeader = objectMapper.readValue(headerBytes.toByteArray(), EncodedFileHeader.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return encodedFileHeader;
    }

}
