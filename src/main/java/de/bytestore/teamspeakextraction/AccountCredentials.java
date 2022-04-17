package de.bytestore.teamspeakextraction;

public class AccountCredentials {
    private String usernameIO;
    private String passwordIO;

    public AccountCredentials(String usernameIO, String passwordIO) {
        this.usernameIO = usernameIO;
        this.passwordIO = passwordIO;
    }

    public String getUsername() {
        return usernameIO;
    }

    public String getPassword() {
        return passwordIO;
    }
}
