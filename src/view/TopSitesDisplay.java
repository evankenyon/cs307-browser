package view;

import javafx.scene.control.ChoiceDialog;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TopSitesDisplay {
    private ChoiceDialog<String> topSitesChoice;

    public TopSitesDisplay() {
        topSitesChoice = new ChoiceDialog<>();
        topSitesChoice.getItems().addAll();
    }

    public void setupTopSitesPopup(){
        // Show property was found at
        // https://www.geeksforgeeks.org/javafx-textinputdialog/
        topSitesChoice.show();
    }

    public void updateTopSites(List<URL> topSitesList) {
        System.out.println(topSitesList);
        List<String> topSitesString = new ArrayList<>();
        for(URL url: topSitesList) {
            topSitesString.add(url.toString());
        }
        this.topSitesChoice.getItems().clear();
        this.topSitesChoice.getItems().addAll(topSitesString);
    }
}
