package sample.ciphering.jobs;

import sample.ciphering.cipherers.AES.AESCipherer;
import sample.exception.CipherModeJobDoesntExistException;
import sample.model.CipherModes;

public class JobFactory {

    public static CiphererJob produce(AESCipherer cipherer, String spurcePath, String destinationPath, CipherModes mode){

        if(mode.equals(CipherModes.ENCODE)){
            return new EncodeJob(cipherer, spurcePath, destinationPath);
        } else if(mode.equals(CipherModes.DECODE)){
            return new DecodeJob(cipherer, spurcePath, destinationPath);
        }

        throw new CipherModeJobDoesntExistException(mode);

    }

}
