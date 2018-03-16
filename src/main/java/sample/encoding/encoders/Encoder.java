package sample.encoding.encoders;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import sample.Settings;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;

public abstract class Encoder {

    protected Cipher cipher;
    protected SecretKeySpec key;
    private DoubleProperty progress = new SimpleDoubleProperty();

    public Encoder(byte[] sessionKey, String algorithm){
        key = new SecretKeySpec(sessionKey, "AES");
        tryCreateCipher(algorithm);
    }

    public byte[] encode(byte[] data){

        progress.setValue(0);

        int dataSize = data.length;
        int numberOfIterations = dataSize/Settings.DATA_PACKET_SIZE;

        tryInitCipher(Cipher.ENCRYPT_MODE);

        byte[] encodedBytes = new byte[(numberOfIterations+1)*Settings.DATA_PACKET_SIZE];

        for(int i = 0; i < numberOfIterations; i++){
            progress.setValue((i+1)/numberOfIterations);
            byte[] bytes = doStep(data, i);
            System.arraycopy(bytes, 0, encodedBytes, i * Settings.DATA_PACKET_SIZE, bytes.length);
        }

        byte[] finalBytes = tryDoFinal();
        System.arraycopy(finalBytes, 0, encodedBytes, numberOfIterations*Settings.DATA_PACKET_SIZE, finalBytes.length);
        return encodedBytes;
    }

    public byte[] doStep(byte[] packet, int iteration){
        return cipher.update(packet, iteration * Settings.DATA_PACKET_SIZE, Settings.DATA_PACKET_SIZE);
    }

    private byte[] tryDoFinal() {
        byte[] encodedBytes = new byte[0];
        try {
            encodedBytes = cipher.doFinal();
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return encodedBytes;
    }

    public byte[] decode(byte[] encodedData){
        progress.setValue(0);

        int dataSize = encodedData.length;
        int numberOfIterations = dataSize/Settings.DATA_PACKET_SIZE;

        tryInitCipher(Cipher.DECRYPT_MODE);

        for(int i = 0; i < numberOfIterations; i++){
            progress.setValue((i+1)/numberOfIterations);
            doStep(encodedData, i);
        }

        byte[] original = tryDoFinal();
        return original;
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
