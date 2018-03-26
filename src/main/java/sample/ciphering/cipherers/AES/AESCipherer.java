package sample.ciphering.cipherers.AES;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
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
    private DoubleProperty progress = new SimpleDoubleProperty();

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
        progress.setValue(0);

        int dataSize = data.length;
        int numberOfIterations = dataSize/ Settings.DATA_PACKET_SIZE;

        byte[] bytesAfterOperation = new byte[(numberOfIterations+1)*Settings.DATA_PACKET_SIZE];

//        for(int i = 0; i < numberOfIterations; i++){
//            progress.setValue((i+1)/numberOfIterations);
//            byte[] bytes = doStep(data, i);
//            System.arraycopy(bytes, 0, bytesAfterOperation, i * Settings.DATA_PACKET_SIZE, bytes.length);
//        }

        byte[] finalBytes = tryDoFinal(data);
        //System.arraycopy(finalBytes, 0, bytesAfterOperation, 0, finalBytes.length);
        progress.set(1.0);
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

    public double getProgress() {
        return progress.get();
    }

    public DoubleProperty progressProperty() {
        return progress;
    }
}
