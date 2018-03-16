package sample.encoding.jobs;

import sample.encoding.encoders.Encoder;
import sample.exception.CipherModeJobDoesntExistException;
import sample.model.CipherModes;

public class JobFactory {

    public static Job produce(Encoder encoder, byte[] data, CipherModes mode){

        if(mode.equals(CipherModes.ENCRYPT)){
            return new EncodeJob(encoder, data);
        } else if(mode.equals(CipherModes.DECRYPT)){
            return new DecodeJob(encoder, data);
        }

        throw new CipherModeJobDoesntExistException(mode);

    }

}
