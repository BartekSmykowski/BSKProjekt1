package sample.ciphering.cipherers;

import sample.exception.NoSuchKeyTypeException;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class UtilKeyFactory {

    public static Key produce(KeyTypes keyType, byte[] keyBytes){

        if(keyType.equals(KeyTypes.PRIVATE)){
            try {
                KeyFactory kf = KeyFactory.getInstance("RSA");
                return kf.generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                e.printStackTrace();
                return null;
            }
        } else if(keyType.equals(KeyTypes.PUBLIC)){
            try {
                KeyFactory kf = KeyFactory.getInstance("RSA");
                return kf.generatePublic(new X509EncodedKeySpec(keyBytes));
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                e.printStackTrace();
                return null;
            }
        } else if(keyType.equals(KeyTypes.SECRET_AES_SPEC)){
            return new SecretKeySpec(keyBytes, "AES");
        }

        throw new NoSuchKeyTypeException(keyType.toString());
    }

}
