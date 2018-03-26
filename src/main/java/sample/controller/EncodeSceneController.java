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

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
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
            checkBox.setSelected(true);
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
        encodingModeChoiceBox.getSelectionModel().selectFirst();

        //test
        encodingData.setSaveDirectory(new File(Settings.TEST_ENCODED_FILE_DIRECTORY));
        encodingData.setSelectedFile(new File(Settings.TEST_ORIGINAL_FILE));
        newFileNameTextField.setText("encoded");

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
        encodingData.setSaveFileName(newFileNameTextField.getText());
        byte[] sessionKey = generateSessionKey();
        encodingData.setSessionKey(sessionKey);
        encodingData.setInitialVector(generateInitialVector());

        Map<String, byte[]> selectedUsersWithKeys = new HashMap<>();
        try {
            for(User user : getSelectedUsers()){
                byte[] userPublicKey = usersMap.get(user.getLogin()).getPublicRsaKey();
                Cipher cipher = Cipher.getInstance("AES");


                KeyFactory kf = KeyFactory.getInstance("AES"); // or "EC" or whatever
                PublicKey publicRSA = kf.generatePublic(new X509EncodedKeySpec(userPublicKey));

                cipher.init(Cipher.ENCRYPT_MODE, publicRSA);
                byte[] encodedSessionKey = cipher.doFinal(sessionKey);
                selectedUsersWithKeys.put(user.getLogin(), encodedSessionKey);
            }
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        encodingData.setAllowedUsersWithSessionKeys(selectedUsersWithKeys);
        if(encodingData.isValid()){
            ScenesManager.setScene(ScenesNames.ENCODING_PROGRESS, new ProgressScene(new EncodeManager(encodingData)));
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
