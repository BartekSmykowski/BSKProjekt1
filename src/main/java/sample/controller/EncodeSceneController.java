package sample.controller;

import javafx.scene.input.MouseEvent;
import sample.scenesManage.ScenesManager;
import sample.scenesManage.ScenesNames;

public class EncodeSceneController {
    public void mainMenu(MouseEvent mouseEvent) {
        ScenesManager.setScene(ScenesNames.MENU);
    }

    public void chooseFile(MouseEvent mouseEvent) {
    }

    public void chooseSaveDirectory(MouseEvent mouseEvent) {
    }

    public void encode(MouseEvent mouseEvent) {
    }
}
