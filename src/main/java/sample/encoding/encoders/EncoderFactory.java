package sample.encoding.encoders;

import sample.exception.NoSuchEncodingModeException;
import sample.model.EncodingModes;

public class EncoderFactory {

    public static Encoder produce(EncodingModes mode, byte[] sessionKey, byte[] initialVector){

        if(mode.equals(EncodingModes.ECB)){
            return new ECBEncoder(sessionKey);
        } else if(mode.equals(EncodingModes.CBC)){
            return new CBCEncoder(sessionKey, initialVector);
        }
        throw new NoSuchEncodingModeException(mode.toString());
    }


}
