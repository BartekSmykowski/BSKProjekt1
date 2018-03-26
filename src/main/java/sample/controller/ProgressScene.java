package sample.controller;

import javafx.beans.property.DoubleProperty;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import sample.ciphering.cipherManagers.CipherManager;
import sample.scenesManage.ScenesManager;
import sample.scenesManage.ScenesNames;

public class ProgressScene {

    public ProgressBar encodingProgressBar;
    public Button menuButton;

    private CipherManager manager;

    public ProgressScene(CipherManager manager){
        this.manager = manager;
    }

    public void initialize(){
        menuButton.setVisible(false);

        DoubleProperty progressProperty = encodingProgressBar.progressProperty();
        progressProperty.bind(manager.getJobExecutor().progressProperty());

        progressProperty
                .addListener((o, oldVal, newVal) -> {
                        if((Double)newVal == 1.0){
                            menuButton.setVisible(true);
                        }
                    });

        manager.performJob();
    }

    public void mainMenu(){
        ScenesManager.setScene(ScenesNames.MENU);
    }

}
