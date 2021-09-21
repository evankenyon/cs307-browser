package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import util.ButtonMaker;

/**
 * Purpose: Represent a home frontend component that allows a user to
 * click a set home button or go to their set home site
 * Assumptions: Properly used within NanoBrowserDisplay (i.e. enableGoHomeButton
 * is only called when it should actually be enabled)
 * Dependencies: JavaFX, ButtonMaker
 * Example: Construct a HomeDisplay object for use in NanoBrowserDisplay to allow
 * for the user to interact with a frontend display that allows them to set and visit
 * a home site
 *
 * @Author Evan Kenyon
 */
public class HomeDisplay {
    private Button goHomeButton;
    private Button setHomeButton;

    /**
     * Purpose: Construct a FavoritesDisplay object with handlers for selecting a favorite site to visit
     * and for setting a favorite site
     * @param setHomeButtonHandler the event handler for when the user clicks the setHomeButton button
     *                           to set their home site
     * @param goHomeButtonHandler the event handler for when the user clicks the setHomeButton button
     *                          to go to their set home site
     * @throws NullPointerException thrown if setHomeButtonHandler or goHomeButtonHandler is null
     */
    public HomeDisplay(EventHandler<ActionEvent> setHomeButtonHandler, EventHandler<ActionEvent> goHomeButtonHandler) throws NullPointerException{
        if(setHomeButtonHandler == null || goHomeButtonHandler == null) {
            throw new NullPointerException();
        }
        setHomeButton = ButtonMaker.makeButton("Set Home", setHomeButtonHandler);
        goHomeButton = ButtonMaker.makeButton("Go Home", goHomeButtonHandler);
        goHomeButton.setDisable(true);
    }

    /**
     * Purpose: Enables the user to press the goHomeButton
     */
    public void enableGoHomeButton() {
        goHomeButton.setDisable(false);
    }

    /**
     * Purpose: Gets the home display components wrapped up in an HBox
     * @return the home display components wrapped up in an HBox
     */
    public Node getHomeDisplay() {
        return new HBox(setHomeButton, goHomeButton);
    }
}
