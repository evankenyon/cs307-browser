package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.AddFavorite;
import util.ButtonMaker;

import java.net.MalformedURLException;

/**
 * Purpose:
 * Assumptions:
 * Dependencies:
 * Example:
 * Other details:
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
     * Purpose:
     * Assumptions:
     * @param selectFavoriteEvent
     * @param onCloseEvent
     */
    // Set up code was borrowed from
    // https://www.geeksforgeeks.org/javafx-textinputdialog/
    public FavoritesDisplay(EventHandler<ActionEvent> selectFavoriteEvent, EventHandler<DialogEvent> onCloseEvent) {
        instantiateNonButtons();
        chooseFavoriteSite.setOnAction(event -> setSelectFavoriteButtonDisable());
        setupButtons(selectFavoriteEvent);
        setNameInput.setHeaderText("Enter the name that you want to refer to this webpage as:");
        setNameInput.setOnCloseRequest(onCloseEvent);
        // Borrowed code for line below from
        // https://stackoverflow.com/questions/31673853/javafx-how-to-know-if-cancel-was-pressed
        setNameInput.getDialogPane().lookupButton(ButtonType.CANCEL).addEventFilter(ActionEvent.ACTION, event -> wasCancelPressed = true);
    }

    /**
     * Purpose:
     * Assumptions:
     * @return
     */
    public Node getDisplayComponentsLeftPanel() {
        return new VBox(addFavoriteButton, description, chooseFavoriteSite, selectFavoriteButton);
    }

    /**
     * Purpose:
     * Assumptions:
     * @return
     * @throws IllegalAccessException
     */
    public String getSiteToVisit() throws IllegalAccessException{
        if(chooseFavoriteSite.getValue().equals("")) {
            throw new IllegalAccessException();
        }
        return chooseFavoriteSite.getValue();
    }

    /**
     * Purpose:
     * Assumptions:
     * @param addFavorite
     * @param myURLDisplay
     * @throws IllegalAccessException
     * @throws MalformedURLException
     */
    public void addFavoriteRefToBrowser(AddFavorite addFavorite, TextField myURLDisplay) throws IllegalAccessException, MalformedURLException {
        if(!wasCancelPressed) {
            updateFavoriteChoices();
            addFavorite.addReferenceToMap(getInput(), myURLDisplay.getText());
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

    // Change exception?
    private String getInput() throws IllegalAccessException {
        if(setNameInput.getResult().equals("")) {
            throw new IllegalAccessException();
        }
        return setNameInput.getResult();
    }
}
