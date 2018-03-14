package sample.encoding;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import sample.model.EncodingData;

import java.util.concurrent.Callable;

public abstract class EncoderJob implements Callable<Object> {

    protected DoubleProperty progress = new SimpleDoubleProperty();
    private EncodingData encodingData;

    public EncoderJob(EncodingData encodingData){
        this.encodingData = encodingData;
    }

    @Override
    public Object call() throws Exception {
        for(int i = 0; i < 100; i++){
            doStep();
            progress.setValue((double)(i+1)/100);
        }
        return null;
    }

    public abstract void doStep();

    public DoubleProperty progressProperty() {
        return progress;
    }

}
