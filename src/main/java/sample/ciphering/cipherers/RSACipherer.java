package sample.ciphering.cipherers;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class RSACipherer implements Cipherer{

    private Cipher cipher;
    private Key key;

    public RSACipherer(KeyTypes keyType, byte[] key){
        tryCreateCipher("RSA");
        this.key = UtilKeyFactory.produce(keyType, key);
    }

    public byte[] encode(byte[] data){
        tryInitCipher(Cipher.ENCRYPT_MODE, key);
        return tryDoFinal(data);
    }

    public byte[] decode(byte[] encodedData){
        tryInitCipher(Cipher.DECRYPT_MODE, key);
        return tryDoFinal(encodedData);
    }

    private void tryInitCipher(int decryptMode, Key key) {
        try {
            cipher.init(decryptMode, key);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    private byte[] tryDoFinal(byte[] data) {
        byte[] encodedBytes = new byte[0];
        try {
            encodedBytes = cipher.doFinal(data);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return encodedBytes;
    }

    private void tryCreateCipher(String algorithm) {
        try {
            cipher = Cipher.getInstance(algorithm);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }
}
