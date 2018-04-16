package sample.controller;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import sample.ciphering.cipherManagers.CipherManager;
import sample.scenesManage.ScenesManager;
import sample.scenesManage.ScenesNames;

public class ProgressScene {

    public ProgressBar progressBar;
    public Button menuButton;
    public Label sourceFilePath;
    public Label destinationFilePath;
    public Label fileSize;
    public Label timeLabel;

    private long startTime;

    private CipherManager manager;

    public ProgressScene(CipherManager manager){
        this.manager = manager;
    }

    public void initialize(){
        menuButton.setVisible(false);
        timeLabel.setText("0.0");
        startTime = System.currentTimeMillis();

        sourceFilePath.setText(manager.getSourcePath());
        destinationFilePath.setText(manager.getDestinationPath());
        fileSize.setText(String.valueOf(manager.getFileSize()));

        DoubleProperty progressProperty = progressBar.progressProperty();
        progressProperty.bind(manager.getJobExecutor().progressProperty());

        progressProperty
                .addListener((o, oldVal, newVal) -> {
                        if((Double)newVal == 1.0){
                            menuButton.setVisible(true);
                            long time = System.currentTimeMillis();
                            double duration = (time - startTime) / 1000.0;
                            Platform.runLater(() -> timeLabel.setText((String.valueOf(duration) + " sec")));
                        }
                    });

        manager.performJob();
    }

    public void mainMenu(){
        ScenesManager.setScene(ScenesNames.MENU);
    }

}
