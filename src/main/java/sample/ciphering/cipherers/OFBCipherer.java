package sample.ciphering.cipherers;

import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;

public class OFBCipherer extends Cipherer {
    private IvParameterSpec ivParameterSpec;

    public OFBCipherer(byte[] key, byte[] initialVector) {
        super(key, "AES/OFB/NoPadding");
        ivParameterSpec = new IvParameterSpec(initialVector);
    }

    @Override
    protected void tryInitCipher(int decryptMode) {
        try {
            cipher.init(decryptMode, key, ivParameterSpec);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }
}
