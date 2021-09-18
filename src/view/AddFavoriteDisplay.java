package view;

import javafx.event.EventHandler;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.TextInputDialog;
import model.NanoBrowser;

public class AddFavoriteDisplay {
    private TextInputDialog setNameInput;


    // Set up code was borrowed from
    // https://www.geeksforgeeks.org/javafx-textinputdialog/
    public AddFavoriteDisplay(EventHandler<DialogEvent> onCloseEvent) {
        setNameInput = new TextInputDialog();
        setNameInput.setHeaderText("Enter the name that you want to refer to this webpage as:");
        setNameInput.setOnCloseRequest(onCloseEvent);
    }

    public void setupAddFavoritePopup(){
        // Show property was found at
        // https://www.geeksforgeeks.org/javafx-textinputdialog/
        setNameInput.show();
    }

    public String getInput() {
        return setNameInput.getResult();
    }
}
