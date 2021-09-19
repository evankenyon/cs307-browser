package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import util.ButtonMaker;

import java.net.URL;
import java.util.List;

public class TopSitesDisplay {
    private Text description;
    private Text breakLine;
    private ListView<URL> topSitesList;
    private Button goToTopSiteButton;

    public TopSitesDisplay(EventHandler<ActionEvent> onPressButton) {
        description = new Text("Most viewed webpages:");
        breakLine = new Text();
        topSitesList = new ListView<>();
        goToTopSiteButton = ButtonMaker.makeButton("Go to selected site", onPressButton);
        goToTopSiteButton.getOnAction();
    }

    public Node getTopSitesDisplay() {
        return new VBox(description, breakLine, topSitesList, goToTopSiteButton);
    }

    public URL getSelectedSite() {
        return topSitesList.getSelectionModel().getSelectedItem();
    }

    public void updateTopSites(List<URL> topSitesList) {
        this.topSitesList.getItems().clear();
        this.topSitesList.getItems().addAll(topSitesList);
    }
}
