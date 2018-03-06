package sample.controller;

import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import sample.scenesManage.ScenesManager;
import sample.scenesManage.ScenesNames;

public class MainSceneController {

    public void registerScene(MouseEvent mouseEvent){
        ScenesManager.setScene(ScenesNames.REGISTER);
    }

    public void encodeScene(MouseEvent mouseEvent) {
        ScenesManager.setScene(ScenesNames.ENCODING);
    }

    public void decodeScene(MouseEvent mouseEvent) {
        ScenesManager.setScene(ScenesNames.DECODING);
    }

    public void quit(MouseEvent mouseEvent) {
        Platform.exit();
    }
}
