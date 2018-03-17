package sample.encoding.encoders;

import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;

public class CFBEncoder extends Encoder {
    private IvParameterSpec ivParameterSpec;

    public CFBEncoder(byte[] sessionKey, byte[] initialVector) {
        super(sessionKey, "AES/CFB/NoPadding");
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
