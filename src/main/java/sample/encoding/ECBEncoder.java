package sample.encoding;

public class ECBEncoder extends Encoder {

    public ECBEncoder(byte[] sessionKey, byte[] initialVector) {
        super(sessionKey, "AES/ECB/PKCS7Padding", initialVector);
    }

}
