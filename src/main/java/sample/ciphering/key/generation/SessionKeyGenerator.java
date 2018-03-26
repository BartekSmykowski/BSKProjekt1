package sample.ciphering.key.generation;

public class SessionKeyGenerator {
    private RandomBytesGenerator randomBytesGenerator;
    private int size;

    public SessionKeyGenerator(int size){
        randomBytesGenerator = new RandomBytesGenerator(size);
    }

    public byte[] generate(){
        return randomBytesGenerator.generate();
    }


}
