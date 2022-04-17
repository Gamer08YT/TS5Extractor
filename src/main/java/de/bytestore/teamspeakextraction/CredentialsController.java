package de.bytestore.teamspeakextraction;

import com.google.protobuf.UnknownFieldSet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.function.Consumer;

public class CredentialsController {
    public static AccountCredentials credentialsIO;

    @FXML
    private TextField usernameIO;

    @FXML
    private TextField passwordIO;

    @FXML
    private TextArea logIO;

    public void initialize() {
        // initialization here, if needed...
        usernameIO.setText(CredentialsController.credentialsIO.getUsername());
        passwordIO.setText(CredentialsController.credentialsIO.getPassword());

        // Store Debug Logs.
        StringBuilder debugIO = new StringBuilder();

        // Build Debug Log.
        HelloApplication.debugIO.forEach(new Consumer<String>() {
            @Override
            public void accept(String logIO) {
                debugIO.append(logIO + "\n");
            }
        });

        // Set Log in TextArea.
        logIO.setText(debugIO.toString());
    }

}
