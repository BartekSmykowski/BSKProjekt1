package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import sample.model.EncodingData;
import sample.model.EncodingModes;
import sample.model.User;
import sample.persistence.UsersLoader;
import sample.scenesManage.ScenesManager;
import sample.scenesManage.ScenesNames;

import java.io.File;
import java.util.*;

public class EncodeSceneController {
    public TextField newFileNameTextField;
    public Label chosenFileLabel;
    public Label fileSizeLabel;
    public Label extensionLabel;
    public Label encodingModeLabel;
    public ChoiceBox<EncodingModes> encodingModeChoiceBox;
    public ListView<CheckBox> encodingUsersListView;
    public Label saveDirectoryLabel;
    public Label errorLabel;


    private EncodingData encodingData;

    private ObservableList<CheckBox> usersCheckBoxList = FXCollections.observableList(new ArrayList<>());
    private Map<String, User> usersMap = new HashMap<>();

    public void initialize(){
        UsersLoader usersLoader = new UsersLoader();
        Collection<User> users = usersLoader.loadUsers();
        users.forEach(user -> {
            CheckBox checkBox = new CheckBox(user.getLogin());
            usersCheckBoxList.add(checkBox);
            usersMap.put(user.getLogin(), user);
        });

        encodingUsersListView.setItems(usersCheckBoxList);

        encodingData = new EncodingData();
        encodingModeChoiceBox.getItems().setAll(EncodingModes.values());
        encodingModeChoiceBox
                .getSelectionModel()
                .selectedIndexProperty()
                .addListener((observableValue, oldVal, newVal) -> {
                    EncodingModes encodingMode = encodingModeChoiceBox.getItems().get((Integer) newVal);
                    encodingModeLabel.setText(encodingMode.toString());
                    encodingData.setEncodingMode(encodingMode);
                });
    }

    public void mainMenu() {
        ScenesManager.setScene(ScenesNames.MENU);
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

    public void encode() {
        encodingData.setAllowedUsers(getSelectedUsers());
        if(encodingData.isValid()){
            ScenesManager.setScene(ScenesNames.ENCODING_PROGRESS, new EncodingProgressScene(encodingData));
        } else {
            errorLabel.setText("Złe dane.");
        }
    }

    private List<User> getSelectedUsers(){
        List<User> selectedUsers = new ArrayList<>();
        usersCheckBoxList.forEach(checkBox -> {
            if(checkBox.isSelected()){
                String login = checkBox.getText();
                selectedUsers.add(usersMap.get(login));
            }
        });
        return selectedUsers;
    }
}
