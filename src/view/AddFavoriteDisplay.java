package view;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Text;

public class AddFavoriteDisplay {
    private TextInputDialog setNameInput;
    private ChoiceBox<String> chooseFavoriteSite;
    private Text description;



    // Set up code was borrowed from
    // https://www.geeksforgeeks.org/javafx-textinputdialog/
    public AddFavoriteDisplay(EventHandler<DialogEvent> onCloseEvent) {
        description = new Text("Favorited webpages:");
        setNameInput = new TextInputDialog();
        chooseFavoriteSite = new ChoiceBox<>();
        setNameInput.setHeaderText("Enter the name that you want to refer to this webpage as:");
        setNameInput.setOnCloseRequest(onCloseEvent);
    }

    public void setupAddFavoritePopup(){
        // Show property was found at
        // https://www.geeksforgeeks.org/javafx-textinputdialog/
        setNameInput.show();
    }

    public Node getChooseFavoriteSite() {
        return new Group(description, chooseFavoriteSite);
    }

    public void updateFavoriteChoices() throws IllegalAccessException {
        chooseFavoriteSite.getItems().add(getInput());
    }

    // Change exception?
    public String getInput() throws IllegalAccessException {
        if(setNameInput.getResult().equals("")) {
            throw new IllegalAccessException();
        }
        return setNameInput.getResult();
    }

    public String getSiteToVisit() {
        return chooseFavoriteSite.getValue();
    }
}
