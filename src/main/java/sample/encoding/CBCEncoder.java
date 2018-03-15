package sample.encoding;

public class CBCEncoder extends Encoder{

    public CBCEncoder(byte[] sessionKey, byte[] initialVector) {
        super(sessionKey, "AES/CBC/PKCS5Padding", initialVector);
    }
}
