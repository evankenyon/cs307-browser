package util;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

/**
 * Purpose: Allow for different view components to create buttons that trigger an event on Action
 * Assumptions: ButtonMaker not constructed, other classes just use static method
 * Dependencies: JavaFX
 * Example: Use makeButton static method to make a button with label and ActionEvent EventHandler handler
 *
 * @Author Evan Kenyon
 */
public class ButtonMaker {

    /**
     * Purpose: Make a button with label label and ActionEvent EventHandler handler
     * Assumptions: label is not null/empty and handler is not null
     * @param label button's label text
     * @param handler an event handler for when button is pressed
     * @return Button with label label and ActionEvent Eventhandler handler
     * @throws NullPointerException thrown if label or handler is null
     */
    public static Button makeButton (String label, EventHandler<ActionEvent> handler) throws NullPointerException{
        if(label == null || handler == null) {
            throw new NullPointerException();
        }
        Button result = new Button();
        result.setText(label);
        result.setOnAction(handler);
        return result;
    }
}
