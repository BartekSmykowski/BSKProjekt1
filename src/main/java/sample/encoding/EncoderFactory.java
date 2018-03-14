package sample.encoding;

import sample.exception.NoSuchEncodingModeException;
import sample.model.EncodingData;
import sample.model.EncodingModes;

public class EncoderFactory {

    public static EncoderJob produce(EncodingData encodingData){
        EncodingModes mode = encodingData.getEncodingMode();
        if(mode.equals(EncodingModes.ECB)){
            return new ECBEncoderJob(encodingData);
        }
        throw new NoSuchEncodingModeException(mode.toString());
    }


}
