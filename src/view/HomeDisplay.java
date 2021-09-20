package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import util.ButtonMaker;

/**
 * Purpose:
 * Assumptions:
 * Dependencies:
 * Example:
 * Other details:
 *
 * @Author Evan Kenyon
 */
public class HomeDisplay {
    private Button goHomeButton;
    private Button setHomeButton;

    /**
     * Purpose:
     * Assumptions:
     * @param setHomeButtonEvent
     * @param goHomeButtonEvent
     */
    public HomeDisplay(EventHandler<ActionEvent> setHomeButtonEvent, EventHandler<ActionEvent> goHomeButtonEvent) {
        setHomeButton = ButtonMaker.makeButton("Set Home", setHomeButtonEvent);
        goHomeButton = ButtonMaker.makeButton("Go Home", setHomeButtonEvent);
        goHomeButton.setDisable(true);
    }

    /**
     * Purpose:
     * Assumptions:
     */
    public void enableGoHomeButton() {
        goHomeButton.setDisable(true);
    }

    /**
     * Purpose:
     * Assumptions:
     * @return
     */
    public Node getHomeDisplay() {
        return new HBox(setHomeButton, goHomeButton);
    }
}
