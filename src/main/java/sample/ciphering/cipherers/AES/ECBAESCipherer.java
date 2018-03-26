package sample.ciphering.cipherers.AES;

import java.security.InvalidKeyException;

public class ECBAESCipherer extends AESCipherer {

    public ECBAESCipherer(byte[] key) {
        super(key, "AES/ECB/PKCS5Padding");
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
