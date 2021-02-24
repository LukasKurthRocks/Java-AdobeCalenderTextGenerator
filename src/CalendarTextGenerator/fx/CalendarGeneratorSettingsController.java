package CalendarTextGenerator.fx;

import CalendarTextGenerator.CalendarDateCustomObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

/**
 * @author Lukas on 17.03.2017
 */
public class CalendarGeneratorSettingsController implements Initializable {
    public Button addDateButton;
    public Button removeDateButton;

    @FXML
    private Button closeButton;

    @FXML
    private CheckBox onlyAdobeTaggedText;
    @FXML
    private CheckBox fillMonthWithSpace;

    @FXML
    private TextField nationalHolidayChar;
    @FXML
    private TextField nonNationalHolidayChar;
    @FXML
    private TextField companyOtherChar;
    @FXML
    private TextField specialHolidayChar;
    @FXML
    private TextField birthdayChar;
    @FXML
    private TextField csvCharset;

    @FXML
    private VBox settingsCustomGrid;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // initialize setting values
        Properties props = new Properties();
        try {
            props.loadFromXML(new FileInputStream(CalendarGenerator.MASTER_SETTINGS_NAME + ".xml"));

            if (props.getProperty("chars.national") != null)
                nationalHolidayChar.setText(props.getProperty("chars.national"));
            if (props.getProperty("chars.non_national") != null)
                nonNationalHolidayChar.setText(props.getProperty("chars.non_national"));
            if (props.getProperty("chars.company") != null)
                companyOtherChar.setText(props.getProperty("chars.company"));
            if (props.getProperty("chars.special") != null)
                specialHolidayChar.setText(props.getProperty("chars.special"));
            if (props.getProperty("chars.birthday") != null)
                birthdayChar.setText(props.getProperty("chars.birthday"));
            if (props.getProperty("csv.charset") != null)
                csvCharset.setText(props.getProperty("csv.charset"));
            // fillMonthWithSpace.setSelected(Boolean.parseBoolean(props.getProperty("bool.month_space")));
            // onlyAdobeTaggedText.setSelected(Boolean.parseBoolean(props.getProperty("bool.adobe_tags")));

            // TODO: Custom Object... Not clear if good enough...
            CalendarDateCustomObject<Integer, String, String> newDates = new CalendarDateCustomObject<>();

            // read CUSTOM DATE properties and save values in object list
            Enumeration<?> e = props.propertyNames();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                String value = props.getProperty(key);

                if (key.contains("custom.date_")) {
                    int num = Integer.parseInt(key.split("_")[1]);
                    String[] values = value.split(",");

                    // add custom date to object-list
                    newDates.put(num, values[0], values[1]);
                }
            }

            // add objects from list to date-box
            for (int i = 0; i < newDates.size(); i++) {
                // todo:!!!
                CalendarDateCustomObject<Integer, String, String> newDate = newDates.get(i);

                // Map Format: 0 - 2011-11-11 - 2011-11-11
                if (newDate != null) // todo: !!!
                    addCustomDateBoxWithValues(newDate.getValue1().get(0), newDate.getValue2().get(0), newDate.getId().get(0), i==0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeApplication() {
        // get a handle to the stage
        Stage stage = (Stage) closeButton.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

    public void saveSettings() throws IOException, URISyntaxException {
        try {
            Properties props = new Properties();

            // load existing props
            props.loadFromXML(new FileInputStream(CalendarGenerator.MASTER_SETTINGS_NAME + ".xml"));

            props.setProperty("info.id", "123456");
            props.setProperty("info.value", "99999");
            props.setProperty("chars.national", nationalHolidayChar.getText());
            props.setProperty("chars.non_national", nonNationalHolidayChar.getText());
            props.setProperty("chars.company", companyOtherChar.getText());
            props.setProperty("chars.special", specialHolidayChar.getText());
            props.setProperty("chars.birthday", birthdayChar.getText());
            /* possible charsets: ISO_8859_1, US_ASCII, UTF_16, UTF_16BE, UTF_16LE, UTF_8 */
            props.setProperty("csv.charset", csvCharset.getText());
            props.setProperty("bool.month_space", String.valueOf(fillMonthWithSpace.isSelected()));
            props.setProperty("bool.adobe_tags", String.valueOf(onlyAdobeTaggedText.isSelected()));

            for (int i = 0; i < settingsCustomGrid.getChildren().size(); i++) {
                HBox dateBox = (HBox) settingsCustomGrid.getChildren().get(i);

                if (((DatePicker) dateBox.getChildren().get(0)).getValue() == null)
                    continue;

                // Get Date From DatePickers (ONE is Label)
                props.setProperty("custom.date_" + i,
                        String.valueOf(((DatePicker) dateBox.getChildren().get(0)).getValue()) + "," +
                        String.valueOf(((DatePicker) dateBox.getChildren().get(2)).getValue()));
            }

            props.storeToXML(new FileOutputStream(CalendarGenerator.MASTER_SETTINGS_NAME + ".xml"),
                    "settings file generated by java.util.properties", "UTF-8");
        } catch (NullPointerException e) {
            UtilsAlerts.showExceptionDialog(e, e.getMessage());
        }

        closeApplication();
    }

    /**
     * Add another box to fill. Accessed via FXML (Java FX).
     */
    public void addCustomDateBox() {
        addCustomDateBoxWithValues("", "", 0, false);
    }

    private void addCustomDateBoxWithValues(String date1, String date2, int elementNumber, boolean removeFirst) {
        if (removeFirst)
            settingsCustomGrid.getChildren().remove(0);

        HBox additionalDateBox = new HBox();

        ObservableList<Node> addBoxChildren = FXCollections.observableArrayList();
        DatePicker datePickerL = new DatePicker();
        datePickerL.setId("dpL_" + elementNumber);
        datePickerL.setPrefWidth(135);
        if (date1 != null && !Objects.equals(date1, ""))
            datePickerL.setValue(LocalDate.parse(date1));
        DatePicker datePickerR = new DatePicker();
        datePickerR.setId("dpR_" + elementNumber);
        datePickerR.setPrefWidth(135);
        if (date2 != null && !Objects.equals(date2, ""))
            datePickerR.setValue(LocalDate.parse(date2));
        Label label = new Label("to");
        label.getStyleClass().add("item-title");
        label.setPrefWidth(48);
        label.setPrefHeight(28);
        label.setAlignment(Pos.CENTER); // horizontal
        label.setTextAlignment(TextAlignment.CENTER); // vertical

        addBoxChildren.add(datePickerL);
        addBoxChildren.add(label);
        addBoxChildren.add(datePickerR);

        additionalDateBox.getChildren().addAll(addBoxChildren);
        additionalDateBox.setId("add_" + elementNumber);

        settingsCustomGrid.getChildren().add(additionalDateBox);
    }

    /**
     * Remove last DateBox in order. Accessed via FXML (Java FX).
     * todo: maybe adding checkboxes to choose date to remove...
     */
    public void removeLastCustomDateBox() {
        try {
            settingsCustomGrid.getChildren().remove(settingsCustomGrid.getChildren().size() - 1);

            // change date props
            Properties props = new Properties();

            // load existing props
            props.loadFromXML(new FileInputStream(CalendarGenerator.MASTER_SETTINGS_NAME + ".xml"));

            // props.setProperty("custom.date_" + settings_custom_grid.getChildren().size(), "");
            props.remove("custom.date_" + settingsCustomGrid.getChildren().size());

            props.storeToXML(new FileOutputStream(CalendarGenerator.MASTER_SETTINGS_NAME + ".xml"),
                    "settings file generated by java.util.properties", "UTF-8");
        } catch (ArrayIndexOutOfBoundsException | IOException e) {
            UtilsAlerts.showExceptionDialog(e, e.getMessage());
        }
    }
}
