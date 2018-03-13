package sample.controller;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import sample.model.EncodingData;
import sample.scenesManage.ScenesManager;
import sample.scenesManage.ScenesNames;

public class EncodingProgressScene {


    public Label chosenFileLabel;
    public Label fileSizeLabel;
    public Label extensionLabel;
    public Label encodingModeLabel;
    public Label saveDirectoryLabel;
    public ProgressBar encodingProgressBar;
    public Button menuButton;

    public EncodingData encodingData;

    public EncodingProgressScene(EncodingData encodingData){
        this.encodingData = encodingData;
    }

    public void initialize(){
        menuButton.setVisible(false);

        encodingProgressBar.setOnMouseClicked(event -> menuButton.setVisible(true));

    }

    public void mainMenu(){
        ScenesManager.setScene(ScenesNames.MENU);
    }

    public void setEncodingData(EncodingData encodingData) {
        this.encodingData = encodingData;
    }
}
