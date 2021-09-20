package util;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

/**
 * Purpose:
 * Assumptions:
 * Dependencies:
 * Example:
 * Other details:
 *
 * @Author Evan Kenyon
 */
public class ButtonMaker {

    /**
     * Purpose:
     * Assumptions:
     * @param label
     * @param handler
     * @return
     */
    // Typical code to create button
    public static Button makeButton (String label, EventHandler<ActionEvent> handler) {
        Button result = new Button();
        result.setText(label);
        result.setOnAction(handler);
        return result;
    }
}
