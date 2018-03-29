package sample.ciphering.cipherers.AES;

import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;

public class OFBAESCipherer extends AESCipherer {
    private IvParameterSpec ivParameterSpec;

    public OFBAESCipherer(byte[] key, byte[] initialVector) {
        super(key, "AES/OFB/PKCS5Padding");
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
