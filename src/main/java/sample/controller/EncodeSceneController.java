package sample.controller;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import sample.scenesManage.ScenesManager;
import sample.scenesManage.ScenesNames;

import java.io.File;

public class EncodeSceneController {
    public TextField newFileNameTextField;
    public Label chosenFileLabel;
    public Label fileSizeLabel;
    public Label extensionLabel;
    public Label encodingType;

    private File selectedFile;

    public void mainMenu(MouseEvent mouseEvent) {
        ScenesManager.setScene(ScenesNames.MENU);
    }

    public void chooseFile(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wybierz plik do szyfrowania.");
        selectedFile = fileChooser.showOpenDialog(ScenesManager.getStage());
        if (selectedFile != null) {
            chosenFileLabel.setText(selectedFile.getName());
            double fileSizeMb = (double)selectedFile.length() / 1000000;
            fileSizeLabel.setText(String.format("%.2f", fileSizeMb));
            extensionLabel.setText(getFileExtension(selectedFile));
        }

    }

    private String getFileExtension(File file) {
        String name = file.getName();
        try {
            return name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }

    public void chooseSaveDirectory(MouseEvent mouseEvent) {
    }

    public void encode(MouseEvent mouseEvent) {
    }
}
