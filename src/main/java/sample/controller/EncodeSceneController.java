package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import sample.Settings;
import sample.ciphering.cipherManagers.EncodeManager;
import sample.ciphering.key.generation.InitialVectorGenerator;
import sample.ciphering.key.generation.SessionKeyGenerator;
import sample.model.EncodingModes;
import sample.model.ManagersData.EncodingData;
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
    public Label chooseBlockLengthLabel;
    public ChoiceBox<Integer> blockLengthChoiceBox;


    private EncodingData encodingData;

    private ObservableList<CheckBox> usersCheckBoxList = FXCollections.observableList(new ArrayList<>());
    private Map<String, User> usersMap = new HashMap<>();

    public void initialize(){
        encodingData = new EncodingData();
        encodingData.setBlockLength(Settings.DATA_PACKET_SIZE);
        initUsersMapAndCheckBoxList();
        initBlockLengthChoice();
        initEncodingModeChoiceBox();

        //test
//        encodingData.setSaveDirectory(Settings.TEST_ENCODED_FILE_DIRECTORY);
//        encodingData.setSelectedFile(Settings.TEST_ORIGINAL_FILE);
//        newFileNameTextField.setText("encoded");

    }

    private void initEncodingModeChoiceBox() {
        encodingModeChoiceBox.getItems().setAll(EncodingModes.values());
        encodingModeChoiceBox
                .getSelectionModel()
                .selectedIndexProperty()
                .addListener((observableValue, oldVal, newVal) -> {
                    EncodingModes encodingMode = encodingModeChoiceBox.getItems().get((Integer) newVal);
                    encodingModeLabel.setText(encodingMode.toString());
                    encodingData.setEncodingMode(encodingMode);
                    if(encodingMode.equals(EncodingModes.CFB) || encodingMode.equals(EncodingModes.OFB)){
                        setBlockLengthChoiceVisible(true);
                        blockLengthChoiceBox.getSelectionModel().selectFirst();
                        encodingData.setBlockLength(blockLengthChoiceBox.getSelectionModel().getSelectedItem());
                    } else {
                        setBlockLengthChoiceVisible(false);
                        encodingData.setBlockLength(Settings.DATA_PACKET_SIZE);
                    }
                });
        encodingModeChoiceBox.getSelectionModel().selectFirst();
    }

    private void initUsersMapAndCheckBoxList() {
        UsersLoader usersLoader = new UsersLoader();
        Collection<User> users = usersLoader.loadUsers();
        users.forEach(user -> {
            CheckBox checkBox = new CheckBox(user.getLogin());
            checkBox.setSelected(true);
            usersCheckBoxList.add(checkBox);
            usersMap.put(user.getLogin(), user);
        });
        encodingUsersListView.setItems(usersCheckBoxList);
    }

    private void initBlockLengthChoice() {
        setBlockLengthChoiceVisible(false);
        List<Integer> blockLengths = new ArrayList<>();
        blockLengths.add(16);
        blockLengths.add(32);
        blockLengths.add(64);
        blockLengths.add(128);
        blockLengthChoiceBox.getItems().setAll(blockLengths);
        blockLengthChoiceBox
                .getSelectionModel()
                .selectedIndexProperty()
                .addListener((observableValue, oldVal, newVal) -> {
                    int blockLength = blockLengthChoiceBox.getItems().get((Integer) newVal);
                    encodingData.setBlockLength(blockLength);
                });
        blockLengthChoiceBox.getSelectionModel().selectFirst();
    }

    private void setBlockLengthChoiceVisible(boolean b) {
        chooseBlockLengthLabel.setVisible(b);
        blockLengthChoiceBox.setVisible(b);
    }

    public void mainMenu() {
        ScenesManager.setScene(ScenesNames.MENU);
    }

    public void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wybierz plik do szyfrowania.");
        File selectedFile = fileChooser.showOpenDialog(ScenesManager.getStage());
        if (selectedFile != null) {
            encodingData.setSelectedFile(selectedFile.getAbsolutePath());
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
            encodingData.setSaveDirectory(saveDirectory.getAbsolutePath());
            saveDirectoryLabel.setText(saveDirectory.getAbsolutePath());
        }
    }

    public void encode() {
        encodingData.setSaveFileName(newFileNameTextField.getText());
        byte[] sessionKey = generateSessionKey();

        encodingData.setSessionKey(sessionKey);
        encodingData.setInitialVector(generateInitialVector());

        encodingData.setAllowedUsers(getSelectedUsers());

        if(encodingData.isValid()){
            ScenesManager.setScene(ScenesNames.PROGRESS, new ProgressScene(new EncodeManager(encodingData)));
        } else {
            errorLabel.setText("Złe dane.");
        }
    }

    private byte[] generateInitialVector() {
        InitialVectorGenerator initialVectorGenerator = new InitialVectorGenerator(Settings.INITIAL_VECTOR_SIZE);
        return initialVectorGenerator.generate();
    }

    private byte[] generateSessionKey() {
        SessionKeyGenerator sessionKeyGenerator = new SessionKeyGenerator(Settings.SESSION_KEY_SIZE);
        return sessionKeyGenerator.generate();
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
