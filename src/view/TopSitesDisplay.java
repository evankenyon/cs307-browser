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

/**
 * Purpose:
 * Assumptions:
 * Dependencies:
 * Example:
 * Other details:
 *
 * @Author Evan Kenyon
 */
public class TopSitesDisplay {
    private Text description;
    private ListView<URL> topSitesList;
    private Button goToTopSiteButton;

    /**
     * Purpose:
     * Assumptions:
     * @param onPressButton
     */
    public TopSitesDisplay(EventHandler<ActionEvent> onPressButton) {
        description = new Text("Most viewed webpages:");
        topSitesList = new ListView<>();
        topSitesList.setOnMouseClicked(event -> setGoToTopSiteButtonDisabled());
        goToTopSiteButton = ButtonMaker.makeButton("Go to selected site", onPressButton);
        goToTopSiteButton.setDisable(true);
    }

    /**
     * Purpose:
     * Assumptions:
     * @return
     */
    public Node getTopSitesDisplay() {
        return new VBox(description, topSitesList, goToTopSiteButton);
    }

    /**
     * Purpose:
     * Assumptions:
     * @return
     */
    public URL getSelectedSite() {
        return topSitesList.getSelectionModel().getSelectedItem();
    }

    /**
     * Purpose:
     * Assumptions:
     * @param topSitesList
     */
    public void updateTopSites(List<URL> topSitesList) {
        this.topSitesList.getItems().clear();
        this.topSitesList.getItems().addAll(topSitesList);
    }

    private void setGoToTopSiteButtonDisabled() {
        goToTopSiteButton.setDisable(topSitesList.getSelectionModel().getSelectedItem() == null);
    }
}
