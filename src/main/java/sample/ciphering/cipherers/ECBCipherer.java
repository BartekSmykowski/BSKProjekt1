package sample.ciphering.cipherers;

import java.security.InvalidKeyException;

public class ECBCipherer extends Cipherer {

    public ECBCipherer(byte[] key) {
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
