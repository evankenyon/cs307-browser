package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import util.ButtonMaker;

import java.net.URL;

public class HomeDisplay {
    private Button goHomeButton;
    private Button setHomeButton;

    public HomeDisplay(EventHandler<ActionEvent> setHomeButtonEvent, EventHandler<ActionEvent> goHomeButtonEvent) {
        setHomeButton = ButtonMaker.makeButton("Set Home", setHomeButtonEvent);
        goHomeButton = ButtonMaker.makeButton("Go Home", setHomeButtonEvent);
        goHomeButton.setDisable(true);
    }

    public void enableGoHomeButton() {
        goHomeButton.setDisable(true);
    }

    public Node getHomeDisplay() {
        return new HBox(setHomeButton, goHomeButton);
    }
}
