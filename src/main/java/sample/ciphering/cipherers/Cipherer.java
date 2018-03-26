package sample.ciphering.cipherers;

public interface Cipherer {
    byte[] encode(byte[] data);
    byte[] decode(byte[] encodedData);
}
