package sample.ciphering.cipherers;

import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;

public class CFBCipherer extends Cipherer {
    private IvParameterSpec ivParameterSpec;

    public CFBCipherer(byte[] key, byte[] initialVector) {
        super(key, "AES/CFB/NoPadding");
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
