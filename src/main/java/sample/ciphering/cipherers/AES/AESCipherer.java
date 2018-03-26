package sample.ciphering.cipherers.AES;

import sample.Settings;
import sample.ciphering.cipherers.Cipherer;
import sample.ciphering.cipherers.KeyTypes;
import sample.ciphering.cipherers.UtilKeyFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public abstract class AESCipherer implements Cipherer {
    protected Cipher cipher;
    protected Key key;

    public AESCipherer(byte[] key, String algorithm){
        this.key = UtilKeyFactory.produce(KeyTypes.SECRET_AES_SPEC, key);
        tryCreateCipher(algorithm);
    }

    public byte[] encode(byte[] data){

        tryInitCipher(Cipher.ENCRYPT_MODE);

        byte[] encodedBytes = performCiphering(data);
        return encodedBytes;
    }

    public byte[] decode(byte[] encodedData){
        tryInitCipher(Cipher.DECRYPT_MODE);
        byte[] original = performCiphering(encodedData);
        return original;
    }

    private byte[] performCiphering(byte[] data) {

        int dataSize = data.length;

        byte[] finalBytes = tryDoFinal(data);
        return finalBytes;
    }

    public byte[] doStep(byte[] packet, int iteration){
        return cipher.update(packet, iteration * Settings.DATA_PACKET_SIZE, Settings.DATA_PACKET_SIZE);
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

    protected abstract void tryInitCipher(int decryptMode);

    protected void tryCreateCipher(String algorithm) {
        try {
            cipher = Cipher.getInstance(algorithm);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }
}
