package sample.ciphering.key.generation;

import java.awt.*;
import java.util.Random;

public class RandomBytesGenerator {

    private int size;

    public RandomBytesGenerator(int size){
        this.size = size;
    }

    public byte[] generate(){
        byte[] bytes = new byte[size];
        long seed = System.currentTimeMillis();
        Point point = MouseInfo.getPointerInfo().getLocation();
        seed *= point.x * point.y;
        Random random = new Random(seed);
        random.nextBytes(bytes);
        return bytes;
    }

}
