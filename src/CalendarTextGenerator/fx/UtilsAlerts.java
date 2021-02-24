package CalendarTextGenerator.fx;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static javafx.scene.control.Alert.AlertType.*;

@SuppressWarnings("Duplicates")
public class UtilsAlerts {
    public static void showInformation(String dialog, String content) {
        showInformation(dialog, content, null);
    }

    @SuppressWarnings("WeakerAccess")
    public static void showInformation(String dialog, String content, String headerText) {
        Alert alert = new Alert(INFORMATION);
        alert.setTitle(dialog);
        alert.setHeaderText(headerText);
        alert.setContentText(content);

        alert.showAndWait();
    }


    public static void showWarning(String dialog, String content) {
        showWarning(dialog, content, null);
    }

    @SuppressWarnings("WeakerAccess")
    public static void showWarning(String dialog, String content, String headerText) {
        Alert alert = new Alert(WARNING);
        alert.setTitle(dialog);
        alert.setHeaderText(headerText);
        alert.setContentText(content);

        alert.showAndWait();
    }


    public static void showError(String dialog, String content) {
        showError(dialog, content, null);
    }

    @SuppressWarnings("WeakerAccess")
    public static void showError(String dialog, String content, String headerText) {
        Alert alert = new Alert(ERROR);
        alert.setTitle(dialog);
        alert.setHeaderText(headerText);
        alert.setContentText(content);

        alert.showAndWait();
    }

    public static void showExceptionDialog(Exception ex, String contentMessage) {
        showExceptionDialog(ex, null, contentMessage);
    }

    @SuppressWarnings("WeakerAccess")
    public static void showExceptionDialog(Exception ex, String contentMessage, String headerText) {
        Alert alert = new Alert(ERROR);
        alert.setTitle("Exception Dialog");
        alert.setHeaderText(headerText);
        alert.setContentText(contentMessage);

        // Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }

    @SuppressWarnings("WeakerAccess")
    public static int showConfirmation(String dialog, String content, String header) {
        Alert alert = new Alert(CONFIRMATION);
        alert.setTitle(dialog);
        alert.setHeaderText(header);
        alert.setContentText(content);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            return 1;
        } else {
            return 0;
        }
    }

    public static int showConfirmation(String dialog, String content) {
        return showConfirmation(dialog, content, null);
    }

    // create own through extend
    @SuppressWarnings("unused")
    private static void showCustomConfirmation() {
        Alert alert = new Alert(CONFIRMATION);
        alert.setTitle("Confirmation Dialog with Custom Actions");
        alert.setHeaderText("Look, a Confirmation Dialog with Custom Actions");
        alert.setContentText("Choose your option.");

        ButtonType buttonTypeOne = new ButtonType("One");
        ButtonType buttonTypeTwo = new ButtonType("Two");
        ButtonType buttonTypeThree = new ButtonType("Three");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == buttonTypeOne) {
                // ... user chose "One"
            } else if (result.get() == buttonTypeTwo) {
                // ... user chose "Two"
            } else if (result.get() == buttonTypeThree) {
                // ... user chose "Three"
            } else {
                // ... user chose CANCEL or closed the dialog
            }
        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }

    // create own through extend
    @SuppressWarnings("unused")
    private static void getTextInput() {
        TextInputDialog dialog = new TextInputDialog("walter");
        dialog.setTitle("Text Input Dialog");
        dialog.setHeaderText("Look, a Text Input Dialog");
        dialog.setContentText("Please enter your name:");

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        //if (result.isPresent())
        //    System.out.println("Your name: " + result.get());

        // The Java 8 way to get the response value (with lambda expression).
        result.ifPresent(name -> System.out.println("Your name: " + name));
    }

    // create own through extend
    @SuppressWarnings("unused")
    private static void getChoice() {
        List<String> choices = new ArrayList<>();
        choices.add("a");
        choices.add("b");
        choices.add("c");

        ChoiceDialog<String> dialog = new ChoiceDialog<>("b", choices);
        dialog.setTitle("Choice Dialog");
        dialog.setHeaderText("Look, a Choice Dialog");
        dialog.setContentText("Choose your letter:");

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        //if (result.isPresent())
        //    System.out.println("Your choice: " + result.get());

        // The Java 8 way to get the response value (with lambda expression).
        result.ifPresent(letter -> System.out.println("Your choice: " + letter));
    }

    // create own through extend
    @SuppressWarnings("unused")
    private void showLoginDialog() {
        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Login Dialog");
        dialog.setHeaderText("Look, a Custom Login Dialog");

        // Set the icon (must be included in the project).
        dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField();
        username.setPromptText("Username");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);

        // Enable/Disable login button depending on whether a username was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        username.textProperty().addListener((observable, oldValue, newValue)
                -> loginButton.setDisable(newValue.trim().isEmpty()));

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(username::requestFocus);

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType)
                return new Pair<>(username.getText(), password.getText());
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(usernamePassword ->
                System.out.println("Username=" + usernamePassword.getKey() + ", Password=" + usernamePassword.getValue()));
    }

    private void JUST_REFERENCE() {
        TextInputDialog dialog = new TextInputDialog("walter");
        dialog.setTitle("Text Input Dialog");
        dialog.setHeaderText("Look, a Text Input Dialog");
        dialog.setContentText("Please enter your name:");

        // Get the Stage.
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();

        // Add a custom icon.
        stage.getIcons().add(new Image(this.getClass().getResource("login.png").toString()));

        // Setting owner
        // Bug: https://javafx-jira.kenai.com/browse/RT-38895
        //dialog.initOwner(otherStage);

        // Minimal Decorations
        dialog.initStyle(StageStyle.UTILITY);

        // Change modality to Modality.NONE, Modality.WINDOW_MODAL or Modality.APPLICATION_MODAL
        dialog.initModality(Modality.NONE);
    }
}