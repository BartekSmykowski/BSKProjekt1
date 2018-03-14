package sample.encoding;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import sample.model.EncodingData;

import java.util.concurrent.Callable;

public abstract class EncoderJob implements Callable<Object> {

    protected DoubleProperty progress = new SimpleDoubleProperty();
    protected EncodingData encodingData;

    public EncoderJob(EncodingData encodingData){
        this.encodingData = encodingData;
    }

    @Override
    public Object call() throws Exception {
        int iterations = getNumberOfIterations();
        for(int i = 0; i < iterations; i++){
            doStep();
            progress.setValue((double)(i+1)/iterations);
        }
        return null;
    }

    protected abstract void doStep();

    protected abstract int getNumberOfIterations();

    public DoubleProperty progressProperty() {
        return progress;
    }

}
