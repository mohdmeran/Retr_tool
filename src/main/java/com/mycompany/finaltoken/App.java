package com.mycompany.finaltoken;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("primary.fxml"));
        Parent root = loader.load();
        PrimaryController controller = loader.getController();
        controller.setHostServices(getHostServices());
        scene = new Scene(root, 1080, 720);
        stage.setScene(scene);
        stage.show();
        
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
//        Parent root = loader.load();
//        MainController controller = loader.getController();
//        controller.setHostServices(getHostServices());
//        primaryStage.setScene(new Scene(root));
//        primaryStage.show();
    }

    void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}