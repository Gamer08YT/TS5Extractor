package de.bytestore.teamspeakextraction;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class HelloApplication extends Application {
    // Store Debug Logs.
    public static ArrayList<String> debugIO = new ArrayList<>();

    // Store Stage Object.
    public static Stage stageIO;

    public static void debug(String logIO) {
        HelloApplication.debugIO.add(logIO);
        System.out.println(logIO);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("welcome-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 512);
        stage.setTitle("TeamSpeak5 Matrix Tools");
        stage.setScene(scene);
        //stage.setAlwaysOnTop(true);
        stage.setResizable(false);
        stage.getIcons().add(new Image(HelloApplication.class.getResourceAsStream("/de/bytestore/teamspeakextraction/matrix.png")));
        stage.show();

        // Set Stage Global.
        HelloApplication.stageIO = stage;
    }

    public static void main(String[] args) {
        WelcomeController.extractDatabase("C:\\Users\\JanHe\\Downloads\\settings.db");
        //launch();
    }
}
