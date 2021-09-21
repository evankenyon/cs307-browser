package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.FavoritesModel;
import util.ButtonMaker;

import java.net.MalformedURLException;

/**
 * Purpose: Represent a favorite frontend component that allows a user to
 * click a set favorite button or select a favorite site to visit
 * Assumptions: Properly used within NanoBrowserDisplay (i.e. addFavoriteRefToBrowser
 * is only called when the user actually wants to do so)
 * Dependencies: JavaFX, FavoritesModel, ButtonMaker, MalformedURLException
 * Example: Construct a FavoritesDisplay object for use in NanoBrowserDisplay to allow
 * for the user to interact with a frontend display that allows them to set and visit
 * favorite sites
 *
 * @Author Evan Kenyon
 */
public class FavoritesDisplay {
    private TextInputDialog setNameInput;
    private ChoiceBox<String> chooseFavoriteSite;
    private Text description;
    private Button addFavoriteButton;
    private Button selectFavoriteButton;
    private boolean wasCancelPressed;


    /**
     * Purpose: Construct a FavoritesDisplay object with handlers for selecting a favorite site to visit
     * and for setting a favorite site
     * @param selectFavoriteHandler the event handler for when the user clicks the selectFavoriteButton button
     *                              to go to the selected favorite site
     * @param onCloseHandler the event handler for when the user clicks out of the setNameInput TextInputDialog
     *                       in order to either create a new favorite site or to cancel creating a new favorite site
     * @throws NullPointerException thrown if selectFavoriteHandler or onCloseHandler is null
     */
    public FavoritesDisplay(EventHandler<ActionEvent> selectFavoriteHandler, EventHandler<DialogEvent> onCloseHandler) throws NullPointerException{
        if(selectFavoriteHandler == null || onCloseHandler == null) {
            throw new NullPointerException();
        }
        instantiateNonButtons();
        chooseFavoriteSite.setOnAction(event -> setSelectFavoriteButtonDisable());
        setupButtons(selectFavoriteHandler);
        // Set up code was borrowed from
        // https://www.geeksforgeeks.org/javafx-textinputdialog/
        setNameInput.setHeaderText("Enter the name that you want to refer to this webpage as:");
        setNameInput.setOnCloseRequest(onCloseHandler);
        // Borrowed code for line below from
        // https://stackoverflow.com/questions/31673853/javafx-how-to-know-if-cancel-was-pressed
        setNameInput.getDialogPane().lookupButton(ButtonType.CANCEL).addEventFilter(ActionEvent.ACTION, event -> wasCancelPressed = true);
    }

    /**
     * Purpose: Gets all the Nodes of this class wrapped up into a VBox
     * @return all the Nodes of this class wrapped up into a VBox
     */
    public Node getDisplayComponents() {
        return new VBox(addFavoriteButton, description, chooseFavoriteSite, selectFavoriteButton);
    }

    /**
     * Purpose: Gets the site to visit based on which favorite site is selected in the
     * chooseFavoriteSite ChoiceBox
     * @return the site to visit based on which favorite site is selected in the chooseFavoriteSite
     * choiceBox
     * @throws IllegalAccessException thrown if no site is selected
     */
    public String getSiteToVisit() throws IllegalAccessException{
        if(chooseFavoriteSite.getValue().equals("")) {
            throw new IllegalAccessException();
        }
        return chooseFavoriteSite.getValue();
    }

    /**
     * Purpose: Adds the current URL as a favorite with the reference input by the user (both to
     * this object and to favoritesModel since it is running things on the backend)
     * @param favoritesModel FavoritesModel object being used by NanoBrowserDisplay to handle
     *                       favorite backend functionality, used to update backend side of
     *                       adding favorite URL reference
     * @param myURLDisplay NanoBrowserDisplay's URL Display, used for getting URL to map current
     *                     input to
     * @throws IllegalAccessException thrown if user has not input anything for favorite URL reference
     * String
     * @throws MalformedURLException thrown if myURLDisplay's text results in a MalformedURLException
     * when constructed
     */
    public void addFavoriteRefToBrowser(FavoritesModel favoritesModel, TextField myURLDisplay) throws IllegalAccessException, MalformedURLException {
        if(!wasCancelPressed) {
            updateFavoriteChoices();
            favoritesModel.addReferenceToMap(getInput(), myURLDisplay.getText());
        }
    }

    private void instantiateNonButtons() {
        description = new Text("Favorited webpages:");
        setNameInput = new TextInputDialog();
        chooseFavoriteSite = new ChoiceBox<>();
    }

    private void setupButtons(EventHandler<ActionEvent> selectFavoriteEvent) {
        selectFavoriteButton = ButtonMaker.makeButton("Go to selected site", selectFavoriteEvent);
        selectFavoriteButton.setDisable(true);
        addFavoriteButton = ButtonMaker.makeButton("Set current website as favorite", event -> setupAddFavoritePopup());
    }

    private void setSelectFavoriteButtonDisable() {
        selectFavoriteButton.setDisable(setNameInput.getResult().equals(""));
    }

    private void setupAddFavoritePopup(){
        // Show property was found at
        // https://www.geeksforgeeks.org/javafx-textinputdialog/
        setNameInput.show();
    }

    private void updateFavoriteChoices() throws IllegalAccessException {
        chooseFavoriteSite.getItems().add(getInput());
    }

    private String getInput() throws IllegalAccessException {
        if(setNameInput.getResult().equals("")) {
            throw new IllegalAccessException();
        }
        return setNameInput.getResult();
    }
}
