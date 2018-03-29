package sample.ciphering.jobs;

import javafx.beans.property.DoubleProperty;

import java.util.concurrent.Callable;

public interface CiphererJob extends Callable<Void> {
    DoubleProperty progressProperty();
}
