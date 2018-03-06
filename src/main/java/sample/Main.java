package sample;

import javafx.application.Application;
import javafx.stage.Stage;
import sample.scenesManage.ScenesManager;
import sample.scenesManage.ScenesNames;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        ScenesManager.setStage(primaryStage);
        ScenesManager.setScene(ScenesNames.MENU);
        primaryStage.setTitle("Hello World");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
