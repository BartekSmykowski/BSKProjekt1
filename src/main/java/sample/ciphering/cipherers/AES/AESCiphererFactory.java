package sample.ciphering.cipherers.AES;

import sample.exception.NoSuchEncodingModeException;
import sample.model.EncodingModes;

public class AESCiphererFactory {

    public static AESCipherer produce(EncodingModes mode, byte[] sessionKey, byte[] initialVector){

        if(mode.equals(EncodingModes.ECB)){
            return new ECBAESCipherer(sessionKey);
        } else if(mode.equals(EncodingModes.CBC)){
            return new CBCAESCipherer(sessionKey, initialVector);
        } else if(mode.equals(EncodingModes.CFB)){
            return new CFBAESCipherer(sessionKey, initialVector);
        } else if(mode.equals(EncodingModes.OFB)){
            return new OFBAESCipherer(sessionKey, initialVector);
        }
        throw new NoSuchEncodingModeException(mode.toString());
    }


}
