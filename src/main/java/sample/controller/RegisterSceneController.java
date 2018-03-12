package sample.controller;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sample.scenesManage.ScenesManager;
import sample.scenesManage.ScenesNames;

public class RegisterSceneController {
    public TextField loginTextField;
    public PasswordField passwordField;

    public void mainMenu() {
        ScenesManager.setScene(ScenesNames.MENU);
    }

    public void register() {
    	
    }
}
