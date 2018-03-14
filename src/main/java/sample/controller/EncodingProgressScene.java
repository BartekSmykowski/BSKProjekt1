package sample.controller;

import javafx.beans.property.DoubleProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import sample.encoding.Encoder;
import sample.encoding.EncoderFactory;
import sample.encoding.EncoderJob;
import sample.model.EncodingData;
import sample.scenesManage.ScenesManager;
import sample.scenesManage.ScenesNames;

import java.io.File;

public class EncodingProgressScene {

    public Label chosenFileLabel;
    public Label fileSizeLabel;
    public Label extensionLabel;
    public Label encodingModeLabel;
    public Label saveDirectoryLabel;
    public ProgressBar encodingProgressBar;
    public Button menuButton;

    private EncodingData encodingData;

    private Encoder encoder;

    public EncodingProgressScene(EncodingData encodingData){
        this.encodingData = encodingData;
    }

    public void initialize(){
        menuButton.setVisible(false);

        updateLabels();

        EncoderJob encoderJob = EncoderFactory.produce(encodingData);

        DoubleProperty progressProperty = encoderJob.progressProperty();
        encodingProgressBar.progressProperty().bind(progressProperty);

        progressProperty
                .addListener((o, oldVal, newVal) -> {
                        if((Double)newVal == 1.0){
                            menuButton.setVisible(true);
                        }
                    });

        encoder = new Encoder(encoderJob);
        encoder.startEncoding();
    }

    private void updateLabels() {
        chosenFileLabel.setText(encodingData.getSelectedFile().getName());
        double fileSizeMb = (double)encodingData.getSelectedFile().length() / 1000000;
        fileSizeLabel.setText(String.format("%.2f", fileSizeMb));
        extensionLabel.setText(getFileExtension(encodingData.getSelectedFile()));
        encodingModeLabel.setText(encodingData.getEncodingMode().toString());
        saveDirectoryLabel.setText(encodingData.getSaveDirectory().toString());
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        try {
            return name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }

    public void mainMenu(){
        ScenesManager.setScene(ScenesNames.MENU);
    }

    public void setEncodingData(EncodingData encodingData) {
        this.encodingData = encodingData;
    }
}
