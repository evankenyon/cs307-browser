package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.AddFavorite;
import model.NanoBrowser;
import util.ButtonMaker;

import java.net.MalformedURLException;

public class FavoritesDisplay {
    private TextInputDialog setNameInput;
    private ChoiceBox<String> chooseFavoriteSite;
    private Text description;
    private Button addFavoriteButton;
    private Button selectFavoriteButton;


    // Set up code was borrowed from
    // https://www.geeksforgeeks.org/javafx-textinputdialog/
    public FavoritesDisplay(EventHandler<ActionEvent> selectFavoriteEvent, EventHandler<DialogEvent> onCloseEvent) {
        instantiateNonButtons();
        chooseFavoriteSite.setOnAction(event -> setSelectFavoriteButtonDisable());
        setupButtons(selectFavoriteEvent);
        setNameInput.setHeaderText("Enter the name that you want to refer to this webpage as:");
        setNameInput.setOnCloseRequest(onCloseEvent);
    }

    // Rename
    public Node getDisplayComponentsLeftPanel() {
        return new VBox(description, chooseFavoriteSite, selectFavoriteButton);
    }

    public Node getAddFavoriteButton() {
        return addFavoriteButton;
    }

    public String getSiteToVisit() throws IllegalAccessException{
        if(chooseFavoriteSite.getValue().equals("")) {
            throw new IllegalAccessException();
        }
        return chooseFavoriteSite.getValue();
    }

    public void addFavoriteRefToBrowser(AddFavorite addFavorite, TextField myURLDisplay) throws IllegalAccessException, MalformedURLException {
        updateFavoriteChoices();
        addFavorite.addReferenceToMap(getInput(), myURLDisplay.getText());
    }

    private void instantiateNonButtons() {
        description = new Text("Favorited webpages:");
        setNameInput = new TextInputDialog();
        chooseFavoriteSite = new ChoiceBox<>();
    }

    private void setupButtons(EventHandler<ActionEvent> selectFavoriteEvent) {
        addFavoriteButton = ButtonMaker.makeButton("Favorite", event -> setupAddFavoritePopup());
        selectFavoriteButton = ButtonMaker.makeButton("Go to selected site", selectFavoriteEvent);
        selectFavoriteButton.setDisable(true);
        addFavoriteButton = ButtonMaker.makeButton("Set Favorite", event -> setupAddFavoritePopup());
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
