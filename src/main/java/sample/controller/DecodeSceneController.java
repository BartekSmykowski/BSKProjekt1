package sample.controller;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import sample.Settings;
import sample.ciphering.cipherManagers.DecodeManager;
import sample.model.ManagersData.DecodingData;
import sample.model.User;
import sample.persistence.UsersLoader;
import sample.scenesManage.ScenesManager;
import sample.scenesManage.ScenesNames;

import java.io.File;
import java.util.Collection;
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
    public PasswordField passwordTextField;

    private DecodingData decodingData;

    private Map<String, User> usersMap = new HashMap<>();

    public void initialize(){
        decodingData = new DecodingData();
        loadUsersToMap();
        initChoiceBox();

        //test
        usersChoiceBox.getSelectionModel().selectFirst();
        decodingData.setSaveDirectoryPath(Settings.TEST_DECODED_FILE_SAVE_DIRECTORY);
        decodingData.setSelectedFilePath(Settings.TEST_ENCODED_FILE_DIRECTORY + "/" + Settings.TEST_ENCODED_FILE_NAME);
        newFileName.setText(Settings.TEST_DECODED_FILE_NAME);
        passwordTextField.setText("qwe");
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
                    decodingData.setSelectedUser(usersMap.get(selectedUser));
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
        if (selectedFile != null) {
            decodingData.setSelectedFilePath(selectedFile.getAbsolutePath());
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
        if (saveDirectory != null) {
            decodingData.setSaveDirectoryPath(saveDirectory.getAbsolutePath());
            saveDirectoryLabel.setText(saveDirectory.getAbsolutePath());
        }
    }

    public void decode() {
        decodingData.setSaveFileName(newFileName.getText());
        decodingData.setPassword(passwordTextField.getText());
        if(decodingData.isValid()){
            ScenesManager.setScene(ScenesNames.PROGRESS, new ProgressScene(new DecodeManager(decodingData)));
        } else {
            errorLabel.setText("Złe dane.");
        }
    }
}
