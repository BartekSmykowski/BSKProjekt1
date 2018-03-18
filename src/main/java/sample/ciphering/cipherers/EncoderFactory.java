package sample.ciphering.cipherers;

import sample.exception.NoSuchEncodingModeException;
import sample.model.EncodingModes;

public class EncoderFactory {

    public static Cipherer produce(EncodingModes mode, byte[] sessionKey, byte[] initialVector){

        if(mode.equals(EncodingModes.ECB)){
            return new ECBCipherer(sessionKey);
        } else if(mode.equals(EncodingModes.CBC)){
            return new CBCCipherer(sessionKey, initialVector);
        } else if(mode.equals(EncodingModes.CFB)){
            return new CFBCipherer(sessionKey, initialVector);
        } else if(mode.equals(EncodingModes.OFB)){
            return new OFBCipherer(sessionKey, initialVector);
        }
        throw new NoSuchEncodingModeException(mode.toString());
    }


}
