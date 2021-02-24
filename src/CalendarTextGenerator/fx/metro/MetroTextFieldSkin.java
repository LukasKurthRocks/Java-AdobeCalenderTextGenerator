package CalendarTextGenerator.fx.metro;


import javafx.scene.control.TextField;

/**
 * @author pedro_000 on 12/5/13
 */
@SuppressWarnings("unused")
public class MetroTextFieldSkin extends TextFieldWithButtonSkin {
    public MetroTextFieldSkin(TextField textField) {
        super(textField);
    }

    protected void rightButtonPressed() {
        getSkinnable().setText("");
    }
}