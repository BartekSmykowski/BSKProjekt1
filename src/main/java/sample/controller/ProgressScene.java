package sample.controller;

import javafx.beans.property.DoubleProperty;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import sample.ciphering.cipherManagers.Manager;
import sample.scenesManage.ScenesManager;
import sample.scenesManage.ScenesNames;

import java.io.File;

public class ProgressScene {

    public ProgressBar encodingProgressBar;
    public Button menuButton;

    private Manager manager;

    public ProgressScene(Manager manager){
        this.manager = manager;
    }

    public void initialize(){
        menuButton.setVisible(false);

        DoubleProperty progressProperty = encodingProgressBar.progressProperty();
        progressProperty.bind(manager.progressProperty());

        progressProperty
                .addListener((o, oldVal, newVal) -> {
                        if((Double)newVal == 1.0){
                            menuButton.setVisible(true);
                        }
                    });

        manager.performJob();
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

}
