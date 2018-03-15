package sample.encoding;

import sample.exception.NoSuchEncodingModeException;
import sample.model.EncodingModes;

public class EncoderFactory {

    public static Encoder produce(EncodingModes mode, byte[] sessionKey, byte[] initialVector){

        if(mode.equals(EncodingModes.ECB)){
            return new ECBEncoder(sessionKey, initialVector);
        } else if(mode.equals(EncodingModes.CBC)){
            return new CBCEncoder(sessionKey, initialVector);
        }
        throw new NoSuchEncodingModeException(mode.toString());
    }


}
