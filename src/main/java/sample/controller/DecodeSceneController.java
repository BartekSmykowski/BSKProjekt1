package sample.controller;

import javafx.scene.input.MouseEvent;
import sample.scenesManage.ScenesManager;
import sample.scenesManage.ScenesNames;

public class DecodeSceneController {
    public void mainMenu(MouseEvent mouseEvent) {
        ScenesManager.setScene(ScenesNames.MENU);
    }
}
