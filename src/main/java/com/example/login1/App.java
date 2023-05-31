package com.example.login1;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class App extends Application {

    private static final String USERS_FILE = "users.txt";
    private Map<String, String> users = new HashMap<>();

    @Override
    public void start(Stage primaryStage) {
        loadUsers();

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        // Username Label
        Label usernameLabel = new Label("Username:");
        GridPane.setConstraints(usernameLabel, 0, 0);

        // Username Input
        TextField usernameInput = new TextField();
        GridPane.setConstraints(usernameInput, 1, 0);

        // Password Label
        Label passwordLabel = new Label("Password:");
        GridPane.setConstraints(passwordLabel, 0, 1);

        // Password Input
        PasswordField passwordInput = new PasswordField();
        GridPane.setConstraints(passwordInput, 1, 1);

        // Login Button
        Button loginButton = new Button("Login");
        GridPane.setConstraints(loginButton, 1, 2);
        loginButton.setOnAction(e -> {
            String username = usernameInput.getText();
            String password = hashPassword(passwordInput.getText());

            if (users.containsKey(username) && users.get(username).equals(password)) {
                showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome " + username + "!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
            }
        });

        // Sign Up Button
        Button signUpButton = new Button("Sign Up");
        GridPane.setConstraints(signUpButton, 1, 3);
        signUpButton.setOnAction(e -> {
            String username = usernameInput.getText();
            String password = hashPassword(passwordInput.getText());

            if (users.containsKey(username)) {
                showAlert(Alert.AlertType.ERROR, "Sign Up Failed", "Username already exists.");
            } else {
                users.put(username, password);
                saveUsers();
                showAlert(Alert.AlertType.INFORMATION, "Sign Up Successful", "Welcome " + username + "!");
            }
        });

        grid.getChildren().addAll(usernameLabel, usernameInput, passwordLabel, passwordInput, loginButton, signUpButton);

        Scene scene = new Scene(grid, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                users.put(parts[0], parts[1]);
            }
        } catch (IOException e) {
            // Ignore if file doesn't exist
        }
    }

    private void saveUsers() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
            for (Map.Entry<String, String> entry : users.entrySet()) {
                writer.println(entry.getKey() + ":" + entry.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String hashPassword(String password) {
        try {

            //algo MD5/SHA1/SHA256
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
