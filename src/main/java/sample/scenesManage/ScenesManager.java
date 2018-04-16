package sample.scenesManage;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class ScenesManager {

    private static Stage stage;

    private static int width = 600;
    private static int height = 400;

    public static void setScene(ScenesNames name) {
        URL resource = getUrl(name);

        FXMLLoader loader = new FXMLLoader(resource);
        Parent root = tryLoadRoot(loader);

        setScene(root);
    }

    public static void setScene(ScenesNames name, Object controller) {
        URL resource = getUrl(name);

        FXMLLoader loader = new FXMLLoader(resource);
        loader.setController(controller);

        Parent root = tryLoadRoot(loader);

        setScene(root);
    }

    private static URL getUrl(ScenesNames name) {
        Class<ScenesManager> supportClass = ScenesManager.class;
        String path = "/view/" + ScenesNames.getFileName(name);
        return supportClass.getResource(path);
    }

    private static Parent tryLoadRoot(FXMLLoader loader) {
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }

    private static void setScene(Parent root) {
        Scene scene = new Scene(root, width, height);
        scene.getStylesheets().add("/view/styles/generalStyles.css");

        stage.setScene(scene);
    }

    public static void setStage(Stage setStage) {
        stage = setStage;
    }

    public static Stage getStage() {
        return stage;
    }
}
