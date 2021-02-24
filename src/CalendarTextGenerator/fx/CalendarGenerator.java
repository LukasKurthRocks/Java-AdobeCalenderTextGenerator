package CalendarTextGenerator.fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Lukas on 15.03.2017
 */
public class CalendarGenerator extends Application {
    public static final String MASTER_SETTINGS_NAME = "MasterSettings";

    private static Parent root;

    static Parent getRoot() {
        return root;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        root = FXMLLoader.load(getClass().getResource("CalendarGeneratorGUI.fxml"));
        //see doc for usage
        // root.getStylesheets().add("/metro/JMetroDarkTheme.css");

        Scene scene = new Scene(root);
        primaryStage.setTitle("Calendar File Generator v0.79");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String... args) {
        // start via Java
        launch(args);
    }
}
