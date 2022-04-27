package de.bytestore.teamspeakextraction;

/**
 * Read Credentials from (currently chat.teamspeak.com) Matrix Servers.
 */
public class AccountCredentials {
    private String usernameIO;
    private String passwordIO;
    private String homebaseIO;

    private String tokenIO;

    private String deviceIO;

    public AccountCredentials(String usernameIO, String passwordIO) {
        this.usernameIO = usernameIO;
        this.passwordIO = passwordIO;
    }

    public AccountCredentials(String tokenIO) {
        this.tokenIO = tokenIO;
    }

    public String getUsername() {
        return usernameIO;
    }

    public String getPassword() {
        return passwordIO;
    }

    public String getHomebase() {
        return homebaseIO;
    }

    public void setHomebase(String homebaseIO) {
        this.homebaseIO = homebaseIO;
    }

    public String getDevice() {
        return deviceIO;
    }

    public void setDevice(String deviceIO) {
        this.deviceIO = deviceIO;
    }

    public String getToken() {
        return tokenIO;
    }

    public void setToken(String tokenIO) {
        this.tokenIO = tokenIO;
    }

    public void setUsername(String usernameIO) {
        this.usernameIO = usernameIO;
    }
}
