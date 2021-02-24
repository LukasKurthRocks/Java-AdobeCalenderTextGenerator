package CalendarTextGenerator.fx;

import CalendarTextGenerator.AdobeCalendarGenerator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.net.URL;
import java.time.Year;
import java.util.Objects;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * @author Lukas on 15.03.2017
 */
public class CalendarGeneratorController implements Initializable {
    /**
     * x offset for moving undecorated window
     */
    private double xOffset = 0;
    /**
     * y offset for moving undecorated window
     */
    private double yOffset = 0;

    @FXML
    private Button settingsButton;

    @FXML
    private Label informationWindow;

    @FXML
    private CheckBox alsoNational;

    @FXML
    private ComboBox<String> numberOfMonth;

    @FXML
    private TextField selectedYear;

    @FXML
    private ComboBox<String> federalStates;

    @FXML
    private CheckBox alsoSecondFridays;

    @SuppressWarnings("unused")
    public void closeApplication() {
        try {
            saveGeneratorSettings();
        } catch (IOException e) {
            e.printStackTrace();
            UtilsAlerts.showExceptionDialog(e, e.getMessage());
        }

        System.exit(0);
    }

    private void saveGeneratorSettings() throws IOException {
        Properties prop = new Properties();

        saveGeneratorSettings(prop);
    }

    private void saveGeneratorSettings(Properties props) throws IOException {
        // load existing properties
        props.loadFromXML(new FileInputStream(CalendarGenerator.MASTER_SETTINGS_NAME + ".xml"));

        // and append these properties
        props.setProperty("main.year", selectedYear.getText());
        props.setProperty("main.state", federalStates.getSelectionModel().getSelectedItem());
        props.setProperty("main.month", numberOfMonth.getSelectionModel().getSelectedItem());
        props.setProperty("main.national", String.valueOf(alsoNational.isSelected()));
        props.setProperty("main.show_second_fridays", String.valueOf(alsoSecondFridays.isSelected()));
        saveXML(props);
    }

    private void saveXML(Properties props) throws IOException {
        props.storeToXML(new FileOutputStream(CalendarGenerator.MASTER_SETTINGS_NAME + ".xml"), "saved by main", "UTF-8");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            if(!new File(System.getProperty("user.dir") + File.separator + CalendarGenerator.MASTER_SETTINGS_NAME + ".xml").exists()) {
                // new FileOutputStream(CalendarGenerator.MASTER_SETTINGS_NAME + ".xml");
                Properties props = new Properties();
                saveXML(props);
            }
        } catch (IOException e) {
            UtilsAlerts.showExceptionDialog(e, e.getMessage());
        }

        String year = String.valueOf(Year.now().plusYears(1));
        String state = null;
        String month = null;
        String national = null;
        String fridays = null;

        try {
            Properties initProps = new Properties();
            initProps.loadFromXML(new FileInputStream(CalendarGenerator.MASTER_SETTINGS_NAME + ".xml"));

            if (initProps.getProperty("main.year") != null)
                year = initProps.getProperty("main.year");
            if (initProps.getProperty("main.year") != null)
                state = initProps.getProperty("main.state");
            if (initProps.getProperty("main.month") != null)
                month = initProps.getProperty("main.month");
            if (initProps.getProperty("main.national") != null)
                national = initProps.getProperty("main.national");
            if (initProps.getProperty("main.show_second_fridays") != null)
                fridays = initProps.getProperty("main.show_second_fridays");
        } catch (IOException e) {
            e.printStackTrace();
            UtilsAlerts.showExceptionDialog(e, e.getMessage());
        }

        selectedYear.setText(year);

        // set options for month (12 or 14 month -> dec + jan)
        ObservableList<String> monthOptions =
                FXCollections.observableArrayList(
                        "12",
                        "14"
                );
        numberOfMonth.getItems().addAll(monthOptions);
        if (month == null)
            numberOfMonth.getSelectionModel().selectFirst();
        else
            numberOfMonth.getSelectionModel().select(month);


        // set options for states (ALL??)
        ObservableList<String> stateOptions = FXCollections.observableArrayList(
                "ALL", "NONE",
                "BW", "BY", "BE", "BB", "HB", "HH",
                "HE", "MV", "NI", "NW", "RP", "SL",
                "SN", "ST", "SH", "TH"
        );
        federalStates.getItems().addAll(stateOptions);
        if (state == null)
            federalStates.getSelectionModel().selectFirst();
        else
            federalStates.getSelectionModel().select(state);


        if (national != null)
            alsoNational.setSelected(Boolean.parseBoolean(national));

        if (fridays != null)
            alsoSecondFridays.setSelected(Boolean.parseBoolean(fridays));

        settingsButton.setOnAction((ActionEvent e) -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CalendarGeneratorGUI_Settings.fxml"));
                Parent root1 = fxmlLoader.load();
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setTitle("Settings");
                stage.setScene(new Scene(root1));
                stage.show();

                /* save x and y offset for moving undecorated window */
                root1.setOnMousePressed(event -> {
                    xOffset = stage.getX() - event.getScreenX();
                    yOffset = stage.getY() - event.getScreenY();
                });
                root1.setOnMouseDragged(event -> {
                    stage.setX(event.getScreenX() + xOffset);
                    stage.setY(event.getScreenY() + yOffset);
                });
            } catch (IOException e1) {
                // change to own error window
                e1.printStackTrace();
                UtilsAlerts.showExceptionDialog(e1, e1.getMessage());
            }
        });
    }

    public void saveAsDialog() throws IOException {
        Properties prop = new Properties();
        if(!new File(System.getProperty("user.dir") + File.separator + CalendarGenerator.MASTER_SETTINGS_NAME + ".xml").exists())
            UtilsAlerts.showError("Settings not found", "The Setting xml file was not found.");
        prop.loadFromXML(new FileInputStream(CalendarGenerator.MASTER_SETTINGS_NAME + ".xml"));

        /*
        * GET THE FILE TO SAVE TO
        * */
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save Special Tagged File");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Adobe Tagged TXT Files (*.txt)", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));

        String lastDir = prop.getProperty("main.last_saved_directory");
        if (lastDir != null)
            chooser.setInitialDirectory(new File(lastDir));

        File file;
        // getting the saved last dir als initial directory. Though the folder could be deleted!!
        try {
            file = chooser.showSaveDialog(CalendarGenerator.getRoot().getScene().getWindow());
        } catch (IllegalArgumentException e) {
            UtilsAlerts.showExceptionDialog(e, e.getMessage());

            System.err.println("Exception: " + e.getMessage() + "! Using the system property \"user.dir\" as initial directory");
            informationWindow.setText("Exception: " + e.getMessage() + "! Using the system property \"user.dir\" as initial directory");

            chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
            file = chooser.showSaveDialog(CalendarGenerator.getRoot().getScene().getWindow());
        }

        if (file == null) {
            // UtilsAlerts.showError("file == null", "File operation has been cancelled!");
            informationWindow.setText("File operation has been cancelled!");
            return;
        }

        if (!Objects.equals(file.getParent(), lastDir)) {
            prop.setProperty("main.last_saved_directory", file.getParent());
            saveXML(prop);
        }

        /*
        * GETTING VARIABLES from properties file
        * */

        System.out.println("prop.getProperty(\"main.year\") = " + prop.getProperty("main.year"));
        System.out.println("prop.getProperty(\"chars.special\") = " + prop.getProperty("chars.special"));

        if (prop.getProperty("chars.national") == null ||
                prop.getProperty("chars.non_national") == null ||
                prop.getProperty("chars.company") == null ||
                prop.getProperty("chars.special") == null ||
                prop.getProperty("chars.birthday") == null) {
                    // saveGeneratorSettings(); // useless; needs to be done in INIT plus the settings page!!
                    UtilsAlerts.showError("Properties not properly set!","You must have started using this application. " +
                            "Thank you! Now got to the settings and save. Then EXIT the application and open it again. Have fun!");
                    return;
                }

        // set "all" check box
        String taggedText = AdobeCalendarGenerator.getCalendarAsAdobeTaggedText(
                "Kalendarium " + numberOfMonth.getValue() + " Monate",
                federalStates.getValue(), alsoNational.isSelected(),
                Boolean.parseBoolean(prop.getProperty("bool.month_space")),
                prop.getProperty("chars.national").toCharArray()[0],
                prop.getProperty("chars.non_national").toCharArray()[0],
                prop.getProperty("chars.company").toCharArray()[0],
                prop.getProperty("chars.special").toCharArray()[0],
                prop.getProperty("chars.birthday").toCharArray()[0],
                prop.getProperty("csv.charset"),
                Integer.parseInt(prop.getProperty("main.year")));

        /*
        * SAVE VIA BUFFER WRITER
        * */
        // save file with charset used by adobe tagged text (ANSI)
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "CP1252"));
        out.write(taggedText);
        out.close();

        // todo: remove? see if this always works
        System.out.println(taggedText);

        informationWindow.setText("File has been saved at " + file.getPath());
    }
}
