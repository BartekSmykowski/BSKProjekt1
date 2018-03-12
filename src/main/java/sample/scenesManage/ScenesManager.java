package sample.scenesManage;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ScenesManager {

    private static Stage stage;

    private static Map<ScenesNames, Scene> scenes = new HashMap<>();

    private static int width = 600;
    private static int height = 400;

    public static void setScene(ScenesNames name) {
        if(scenes.containsKey(name)) {
            stage.setScene(scenes.get(name));
        } else {
            Class<ScenesManager> aClass = ScenesManager.class;
            String nameString = "/view/" + ScenesNames.getFileName(name);
            URL resource = aClass.getResource(nameString);
            Parent root = tryLoadRoot(resource);
            Scene scene = new Scene(root, width, height);
            stage.setScene(scene);
            scenes.put(name, scene);
        }
    }

    private static Parent tryLoadRoot(URL resource) {
        Parent root = null;
        try {
            root = FXMLLoader.load(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }

    public static void setStage(Stage setStage) {
        stage = setStage;
    }

    public static Stage getStage() {
        return stage;
    }
}
