package de.bytestore.teamspeakextraction;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.User32;

public class HelloApplication extends Application {
    // Store Debug Logs.
    public static ArrayList<String> debugIO = new ArrayList<>();

    // Store Stage Object.
    public static Stage stageIO;

    public static void debug(String logIO) {
        HelloApplication.debugIO.add(logIO);
        System.out.println(logIO);
    }

    public static void main(String[] args) {
        //WelcomeController.extractDatabase("C:\\Users\\JanHe\\Downloads\\settings.db");
        //WelcomeController.extractDatabase("C:\\Users\\JanHe\\Desktop\\settings.db");
        // ServerCredentials.extractChat("C:\\Users\\JanHe\\AppData\\Local\\TeamSpeak\\Cache\\Default\\tschat\\ZDVlZTE4ZGJjZWU1YzBiNmQ3MDVkODYwMzRjNmNiMWE");
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("welcome-view.fxml"));
        //FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("server-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 512);
        stage.setTitle("TeamSpeak5 Matrix Tools");
        stage.setScene(scene);
        //stage.setAlwaysOnTop(true);
        stage.setResizable(false);
        stage.getIcons().add(new Image(HelloApplication.class.getResourceAsStream("matrix.png")));
        stage.show();

        //Set Windows 10/11 title bar to dark mode
        HWND hwnd = User32.INSTANCE.FindWindow(null, "TeamSpeak5 Matrix Tools");
        DwmApi.setDarkModeTitleBar(hwnd, true);

        // Set Stage Global.
        HelloApplication.stageIO = stage;
    }
}
