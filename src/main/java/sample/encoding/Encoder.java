package sample.encoding;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Encoder {

    protected Cipher cipher;
    private SecretKeySpec key;
    private IvParameterSpec ivParameterSpec;

    public Encoder(byte[] sessionKey, String algorithm, byte[] initialVector){
        key = new SecretKeySpec(sessionKey, "AES");
        tryCreateCipher(algorithm);
        ivParameterSpec = new IvParameterSpec(initialVector);
    }

    public byte[] encode(byte[] data){

        tryInitCipher(Cipher.ENCRYPT_MODE);
        byte[] encodedBytes = tryDoFinal(data);
        return encodedBytes;
    }

    private byte[] tryDoFinal(byte[] dataBytes) {
        byte[] encodedBytes = new byte[0];
        try {
            encodedBytes = cipher.doFinal(dataBytes);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return encodedBytes;
    }

    public byte[] decode(byte[] encodedData){
        tryInitCipher(Cipher.DECRYPT_MODE);
        byte[] original = tryDoFinal(encodedData);
        return original;
    }

    private void tryInitCipher(int decryptMode) {
        try {
            cipher.init(decryptMode, key, ivParameterSpec);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    protected void tryCreateCipher(String algorithm) {
        try {
            cipher = Cipher.getInstance(algorithm);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }
}
