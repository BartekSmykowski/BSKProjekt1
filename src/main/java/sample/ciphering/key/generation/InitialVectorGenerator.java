package sample.ciphering.key.generation;

public class InitialVectorGenerator {
    private RandomBytesGenerator randomBytesGenerator;

    public InitialVectorGenerator(int size){
        randomBytesGenerator = new RandomBytesGenerator(size);
    }

    public byte[] generate(){
        return randomBytesGenerator.generate();
    }

}
