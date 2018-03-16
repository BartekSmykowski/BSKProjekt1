package sample.controller;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import sample.model.CipherModes;
import sample.model.EncodingData;
import sample.model.User;
import sample.persistence.UsersLoader;
import sample.scenesManage.ScenesManager;
import sample.scenesManage.ScenesNames;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DecodeSceneController {
    public TextField newFileName;
    public Label chosenFileLabel;
    public Label fileSizeLabel;
    public Label extensionLabel;
    public Label encodingModeLabel;
    public Label saveDirectoryLabel;
    public Label errorLabel;
    public ChoiceBox<String> usersChoiceBox;

    private EncodingData encodingData;

    private Map<String, User> usersMap = new HashMap<>();

    public void initialize(){
        encodingData = new EncodingData();
        loadUsersToMap();
        initChoiceBox();
    }

    public void mainMenu() {
        ScenesManager.setScene(ScenesNames.MENU);
    }

    private void initChoiceBox() {
        usersChoiceBox.getItems().setAll(usersMap.keySet());
        usersChoiceBox
                .getSelectionModel()
                .selectedIndexProperty()
                .addListener((observableValue, oldVal, newVal) -> {
                    String selectedUser = usersChoiceBox.getItems().get((Integer) newVal);
                    encodingData.setAllowedUsers(Collections.singletonList(usersMap.get(selectedUser)));
                });
    }

    private void loadUsersToMap() {
        UsersLoader usersLoader = new UsersLoader();
        Collection<User> users = usersLoader.loadUsers();
        users.forEach(user -> usersMap.put(user.getLogin(), user));
    }

    public void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wybierz plik do szyfrowania.");
        File selectedFile = fileChooser.showOpenDialog(ScenesManager.getStage());
        encodingData.setSelectedFile(selectedFile);
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

    public void chooseSaveDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Wybierz folder zapisu.");
        File saveDirectory = directoryChooser.showDialog(ScenesManager.getStage());
        encodingData.setSaveDirectory(saveDirectory);
        if (saveDirectory != null) {
            saveDirectoryLabel.setText(saveDirectory.getAbsolutePath());
        }
    }

    public void decode() {
        encodingData.setSaveFileName(newFileName.getText());
        encodingData.setSessionKey(new byte[0]);
        encodingData.setInitialVector(new byte[0]);
        encodingData.setCipherModes(CipherModes.DECRYPT);
        if(encodingData.isValid()){
            ScenesManager.setScene(ScenesNames.ENCODING_PROGRESS, new EncodingProgressScene(encodingData));
        } else {
            errorLabel.setText("Złe dane.");
        }
    }
}
