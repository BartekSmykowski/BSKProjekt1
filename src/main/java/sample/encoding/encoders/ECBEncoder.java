package sample.encoding.encoders;

import java.security.InvalidKeyException;

public class ECBEncoder extends Encoder {

    public ECBEncoder(byte[] sessionKey) {
        super(sessionKey, "AES/ECB/PKCS5Padding");
    }

    @Override
    protected void tryInitCipher(int decryptMode) {
        try {
            cipher.init(decryptMode, key);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }
}
