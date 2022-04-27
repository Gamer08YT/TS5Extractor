package de.bytestore.teamspeakextraction;

import com.google.protobuf.ByteString;
import com.google.protobuf.UnknownFieldSet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.List;

public class WelcomeController {
    @FXML
    private TextField pathIO;

    @FXML
    private CheckBox agreeIO;

    // Store Connection of SQLite.
    private static Connection connectionIO;

    @FXML
    protected void onExplorer() {
        HelloApplication.debug("Selecting Database.");

        // Create a new File Chooser.
        FileChooser chooserIO = new FileChooser();

        // Get Default Filter List.
        List<FileChooser.ExtensionFilter> filtersIO = chooserIO.getExtensionFilters();

        // Clear Filter List.
        filtersIO.clear();

        // Add Database Filter.
        filtersIO.add(new FileChooser.ExtensionFilter("TeamSpeak Settings Database", "settings.db"));

        // Open File Selector.
        File databaseIO = chooserIO.showOpenDialog(HelloApplication.stageIO);

        // Set Path of File to Input Field.
        if (databaseIO != null) {
            pathIO.setText(databaseIO.getPath());
        }
    }

    public static AccountCredentials extractDatabase(String pathIO) {
        HelloApplication.debug("Reading Database.");

        try {
            // Load Driver Class.
            Class.forName("org.sqlite.JDBC");

            // Load File from Database.
            connectionIO = DriverManager.getConnection("jdbc:sqlite:" + pathIO);

            // Prepare Statement.
            PreparedStatement statementIO = connectionIO.prepareStatement("SELECT * FROM `ProtobufItems`");

            // Query for Data.
            ResultSet setIO = statementIO.executeQuery();

            // For Debug.
            int countIO = 0;

            // Foreach Row in Database.
            while (setIO.next()) {
                // Get Blob from Entry.
                byte[] blobIO = setIO.getBytes("value");

                // Check if Blcb is not empty.
                if (blobIO != null) {
                    try {
                        // Convert Blob to Data.
                        String decodeIO = UnknownFieldSet.parseFrom(blobIO).toString();

                        if (decodeIO.contains("11111111-aaaa-aaaa-aaaa-aaaaaaaaaaaa")) {
                            // Get Builder from ProtoBuf.
                            UnknownFieldSet builderIO = UnknownFieldSet.parseFrom(blobIO);

                            // Check if Entry 21 exits.
                            if (builderIO.asMap().containsKey(21)) {
                                // Print Debug Message.
                                HelloApplication.debug("Found Matrix Entry 21.");

                                // Get Entry 21 From ProtoBuf.
                                UnknownFieldSet.Field lineIO = builderIO.asMap().get(21);

                                // Store credentials.
                                if (lineIO.getLengthDelimitedList().size() > 0) {

                                    for (ByteString keyIO : lineIO.getLengthDelimitedList()) {
                                        // Get Builder from ProtoBuf.
                                        UnknownFieldSet probufIO = UnknownFieldSet.parseFrom(keyIO.toByteArray());

                                        for (ByteString rowIO : probufIO.getField(5).getLengthDelimitedList()) {
                                            // Handle Data.
                                            String dataIO = rowIO.toStringUtf8();

                                            // Set Server.
                                            String serverIO = dataIO.split("\u0012")[1];

                                            // Reset Data.
                                            dataIO = dataIO.replaceAll(serverIO, "");
                                            dataIO = dataIO.replaceAll("\u0012", "");
                                            dataIO = dataIO.replaceAll(" ", "");

                                            // Set Username and Password.
                                            String usernameIO = (dataIO.split("\u001A")[0]).replace("\n", "");
                                            String passwordIO = dataIO.split("\u001A")[1];

                                            System.out.println("LENGTH " + usernameIO.length());

                                            // Remove first 8 from Username.
                                            if (usernameIO.length() > 56 && usernameIO.startsWith("8")) {
                                                System.out.println("SUBSTRING " + usernameIO.substring(1));
                                                usernameIO = usernameIO.substring(1);
                                            }

                                            // Print Debug Message.
                                            HelloApplication.debug("Found Matrix Entry with Password " + passwordIO + " and Username " + usernameIO + " for Server " + serverIO + ".");

                                            // Return Data to JavaFX.
                                            return new AccountCredentials(usernameIO, passwordIO);
                                        }
                                    }

                                } else
                                    HelloApplication.debug("Matrix Entry size is to low.");
                            }
                        }
                    } catch (IOException e) {
                        // Print Debug Message.
                        HelloApplication.debug("Failed to decode row " + countIO + ". " + e.getMessage());
                    }

                }

            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @FXML
    protected void onToken() {
        // Load new Scene.
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("server-view.fxml"));
        Scene scene = null;

        try {
            scene = new Scene(fxmlLoader.load(), 1024, 512);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Close old Stage.
        HelloApplication.stageIO.setScene(scene);
    }

    @FXML
    protected void onClick() {
        HelloApplication.debug("Extracting Database.");

        // Create new Popup for Agreement Error.
        Alert popupIO = new Alert(Alert.AlertType.INFORMATION);

        // Set Title of Popup.
        popupIO.setTitle("TeamSpeak5 Matrix Extractor");

        // Set Background Color of Popup.
        popupIO.initStyle(StageStyle.TRANSPARENT);

        // Check if Path is not null.
        if (pathIO.getText().isEmpty()) {
            // Set Message.
            popupIO.setContentText("You must select a Database to extract your Credentials.");

            // Show Popup to Client.
            popupIO.show();

            return;
        }

        // Check if File is readable.
        /*if (new File(pathIO.getText()).canRead()) {
            // Set Message.
            popupIO.setContentText("You must close your TeamSpeak to extract your Credentials.");

            // Show Popup to Client.
            popupIO.show();
        }*/

        // Check if Agreement is Selected.
        if (!agreeIO.isSelected()) {
            // Set Message.
            popupIO.setContentText("You must agree to extract your Credentials.");

            // Show Popup to Client.
            popupIO.show();

            return;
        }

        // Extract Database.
        AccountCredentials credentialsIO = WelcomeController.extractDatabase(this.pathIO.getText());

        if (credentialsIO != null) {
            CredentialsController.credentialsIO = credentialsIO;

            // Load new Scene.
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("credentials-view.fxml"));
                Scene scene = null;

                scene = new Scene(fxmlLoader.load(), 1024, 512);

                // Close old Stage.
                HelloApplication.stageIO.setScene(scene);

            } catch (IOException e) {
                // Set Success Alert Type.
                popupIO.setAlertType(Alert.AlertType.ERROR);

                // Set Message.
                popupIO.setContentText("Error while switching Scene.\n" + e.getMessage());

                // Show Popup to Client.
                popupIO.show();
            }
        } else {
            // Set Success Alert Type.
            popupIO.setAlertType(Alert.AlertType.ERROR);

            // Set Message.
            popupIO.setContentText("Error while extracting your Credentials, start Application as CLI and view Logs");

            // Show Popup to Client.
            popupIO.show();
        }
    }

}
