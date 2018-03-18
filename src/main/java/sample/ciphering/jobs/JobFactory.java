package sample.ciphering.jobs;

import sample.ciphering.cipherers.Cipherer;
import sample.exception.CipherModeJobDoesntExistException;
import sample.model.CipherModes;

public class JobFactory {

    public static Job produce(Cipherer cipherer, byte[] data, CipherModes mode){

        if(mode.equals(CipherModes.ENCRYPT)){
            return new EncodeJob(cipherer, data);
        } else if(mode.equals(CipherModes.DECRYPT)){
            return new DecodeJob(cipherer, data);
        }

        throw new CipherModeJobDoesntExistException(mode);

    }

}
