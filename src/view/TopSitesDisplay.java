package view;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.List;

public class TopSitesDisplay {
    private Text description;
    private Text breakLine;
    private ListView<URL> topSitesList;

    public TopSitesDisplay() {
        description = new Text("Most viewed webpages:");
        breakLine = new Text();
        topSitesList = new ListView<>();
    }

    public Node getTopSitesDisplay() {
        return new Group(description, breakLine, topSitesList);
    }

    public URL getSelectedSite() {
        return topSitesList.getSelectionModel().getSelectedItem();
    }

    public void updateTopSites(List<URL> topSitesList) {
        this.topSitesList.getItems().clear();
        this.topSitesList.getItems().addAll(topSitesList);
    }
}
