package de.bytestore.teamspeakextraction;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.StageStyle;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.zip.GZIPInputStream;

/**
 * Read Credentials from (new TS5 Server) Matrix Servers.
 */
public class ServerCredentials {
    // Store Credentials of TSChat.
    public static ArrayList<AccountCredentials> credentialsIO = new ArrayList<AccountCredentials>();
    // Store Items of Server Dropdown.
    private final ObservableList<String> serverIO = FXCollections.observableArrayList();
    // Store ListView Element.
    private final ListView<String> viewIO = new ListView<String>();

    @FXML
    private CheckBox agreeIO;

    @FXML
    private HBox boxIO;

    public static AccountCredentials extractChat(String fileIO) {

        try {
            // Store decoded Content of GZIP File.
            String contentIO = extractGZIP(new File(fileIO));

            if (contentIO != null && !contentIO.isEmpty()) {
                // Store JSON Object of parsed File.
                JsonObject dataIO = JsonParser.parseString(contentIO).getAsJsonObject();

                // Check if Login Data exits.
                if (dataIO.has("login_data")) {
                    // Store Login Data as Object.
                    JsonObject loginIO = dataIO.getAsJsonObject("login_data");

                    // Create new Account Credentials Object.
                    AccountCredentials credentialsIO = new AccountCredentials(loginIO.get("access_token").getAsString());

                    // Set Token of Credentials.
                    credentialsIO.setUsername(loginIO.get("user_id").getAsString());

                    if (loginIO.has("home_server")) {
                        // Set Homebase of Credentials.
                        credentialsIO.setHomebase(loginIO.get("home_server").getAsString());
                    }

                    if (loginIO.has("device_id")) {
                        // Set Device ID of Credentials.
                        credentialsIO.setDevice(loginIO.get("device_id").getAsString());
                    }

                    // Return Credentials Object.
                    return credentialsIO;
                }
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    private static String extractGZIP(File fileIO) throws IOException {
        // Double check is better than no check :)
        if (fileIO.exists()) {
            // Byte Array Buffer for reading.
            // byte[] bufferIO = new byte[1024];

            // Create new Output String for decoded Data.
            StringBuilder decodeIO = new StringBuilder();

            // Create new Byte Array Input Stream.
            ByteArrayInputStream byteIO = new ByteArrayInputStream(Base64.getDecoder().decode(IOUtils.toByteArray(Files.newInputStream(fileIO.toPath()))));

            // Create new Input Stream for GZIP.
            GZIPInputStream gzipIO = new GZIPInputStream(byteIO);

            // Create BufferedReader for Decoding to String.
            BufferedReader readerIO = new BufferedReader(new InputStreamReader(gzipIO, Charset.defaultCharset()));

            // Read File with total Size.
            String lineIO;

            // Read Stream into Buffer until (-1/null) (EOF).
            while ((lineIO = readerIO.readLine()) != null) {
                // Write with no offset.
                decodeIO.append(lineIO);
            }

            // Close all Streams.
            //inputIO.close();
            gzipIO.close();

            // Return decoded Data (not optimized i know ;-|).
            //return new ByteArrayInputStream(decodeIO.toByteArray());
            return decodeIO.toString();
        }

        return null;
    }

    public static boolean isCompressed(final byte[] compressedIO) {
        return (compressedIO[0] == (byte) (GZIPInputStream.GZIP_MAGIC)) && (compressedIO[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8));
    }

    public void initialize() {
        // Store Dropdown View for Servers.
        //ListView<String> viewIO = new ListView<String>();

        // Set Padding of Select.
        viewIO.setPadding(new Insets(8.0, 8.0, 8.0, 8.0));

        // Set Width and Height of Select.
        viewIO.setMaxWidth(650);
        viewIO.setPrefWidth(650);
        viewIO.setMaxHeight(150);
        viewIO.setPrefHeight(150);

        // Scan for Chat Credentials.
        for (File fileIO : new File(System.getenv("LOCALAPPDATA") + "/TeamSpeak/Cache/Default/tschat").listFiles()) {
            try {
                // Get Account Credentials from File.
                AccountCredentials credentialsIO = ServerCredentials.extractChat(fileIO.getPath());

                // Check if Credentials are not empty.
                if (credentialsIO != null) {
                    // Add Select to Items.
                    serverIO.add(credentialsIO.getHomebase() + " | (" + credentialsIO.getDevice() + ")");

                    // Add Credentials to Array.
                    ServerCredentials.credentialsIO.add(credentialsIO);
                }
            } catch (Exception exceptionIO) {
                // Print Debug Message.
                System.out.println(exceptionIO.getMessage());
            }
        }

        // Set Items of View.
        viewIO.setItems(serverIO);

        // Add View to HBox Children.
        boxIO.getChildren().add(viewIO);
    }

    @FXML
    protected void onClick() {
        // Create new Popup for Agreement Error.
        Alert popupIO = new Alert(Alert.AlertType.INFORMATION);

        // Set Title of Popup.
        popupIO.setTitle("TeamSpeak5 Matrix Extractor");

        // Set Background Color of Popup.
        popupIO.initStyle(StageStyle.TRANSPARENT);

        // Check if Item is Selected.
        if (viewIO.getSelectionModel().getSelectedItems().isEmpty()) {
            // Set Message.
            popupIO.setContentText("You must select an Server to extract your Credentials.");

            // Show Popup to Client.
            popupIO.show();

            return;
        }

        // Check if Agreement is Selected.
        if (!agreeIO.isSelected()) {
            // Set Message.
            popupIO.setContentText("You must agree to extract your Credentials.");

            // Show Popup to Client.
            popupIO.show();

            return;
        }

        try {
            // Get Account from Credentials Database.
            AccountCredentials accountIO = ServerCredentials.credentialsIO.get(viewIO.getSelectionModel().getSelectedIndex());

            // Override old Credentials.
            CredentialsController.credentialsIO = accountIO;

            // Load new Scene.
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("credentials-view.fxml"));
            Scene scene = null;

            scene = new Scene(fxmlLoader.load(), 1024, 512);

            // Close old Stage.
            HelloApplication.stageIO.setScene(scene);
        } catch (IndexOutOfBoundsException exceptionIO) {
            // Print Debug Message.
            System.out.println("OutOfBounds.");
        } catch (IOException e) {
            // Set Success Alert Type.
            popupIO.setAlertType(Alert.AlertType.ERROR);

            // Set Message.
            popupIO.setContentText("Error while switching Scene.\n" + e.getMessage());

            // Show Popup to Client.
            popupIO.show();
        }
    }
}
