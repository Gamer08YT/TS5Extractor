package de.bytestore.teamspeakextraction;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.function.Consumer;

public class CredentialsController {
    public static AccountCredentials credentialsIO;

    @FXML
    private TextField usernameIO;

    @FXML
    private TextField homebaseIO;

    @FXML
    private TextField passwordIO;

    @FXML
    private TextArea logIO;

    public void initialize() {
        // initialization here, if needed...
        usernameIO.setText(CredentialsController.credentialsIO.getUsername());
        passwordIO.setText(CredentialsController.credentialsIO.getPassword());
        homebaseIO.managedProperty().bind(homebaseIO.visibleProperty());

        if (CredentialsController.credentialsIO.getToken() != null) {
            passwordIO.setPromptText("Token");
            passwordIO.setText(CredentialsController.credentialsIO.getToken());
            homebaseIO.setText(CredentialsController.credentialsIO.getHomebase());
            homebaseIO.setVisible(true);
        }

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
