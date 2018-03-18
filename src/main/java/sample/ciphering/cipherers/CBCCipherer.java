package sample.ciphering.cipherers;

import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;

public class CBCCipherer extends Cipherer {
    private IvParameterSpec ivParameterSpec;

    public CBCCipherer(byte[] key, byte[] initialVector) {
        super(key, "AES/CBC/PKCS5Padding");
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
