package sample.ciphering.cipherers.AES;

import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;

public class CBCAESCipherer extends AESCipherer {
    private IvParameterSpec ivParameterSpec;

    public CBCAESCipherer(byte[] key, byte[] initialVector) {
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
