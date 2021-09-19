package view;

import java.net.URL;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import model.NanoBrowser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;


/**
 * A class used to display the viewer for a simple HTML browser.
 * 
 * See this tutorial for help on how to use all variety of components:
 *   http://download.oracle.com/otndocs/products/javafx/2/samples/Ensemble/
 * 
 * @author Owen Astrachan
 * @author Marcin Dobosz
 * @author Yuzhang Han
 * @author Edwin Ward
 * @author Robert C. Duvall
 */
public class NanoBrowserDisplay {
    // constants
    public static final String BLANK = " ";

    // web page
    private WebView myPage;
    // navigation
    private TextField myURLDisplay;
    // information area
    private Label myStatus;
    private NanoBrowser nanoBrowser;
    private AddFavoriteDisplay addFavoriteDisplay;
    private TopSitesDisplay topSitesDisplay;
    private Button selectFavoriteButton;
    private Button nextButton;
    private Button backButton;

    /**
     * Create a web browser with prompts in the given language with initially empty state.
     */
    public NanoBrowserDisplay() {
        nanoBrowser = new NanoBrowser();
        topSitesDisplay = new TopSitesDisplay();
    }

    /**
     * Returns scene for the browser, so it can be added to stage.
     */
    public Scene makeScene (int width, int height) {
        BorderPane root = new BorderPane();

        // must be first since other panels may refer to page
        root.setCenter(makePageDisplay());
        root.setTop(makeInputPanel());
        root.setBottom(makeInformationPanel());
        root.setLeft(makeLeftPanel());
        // create scene to hold UI
        return new Scene(root, width, height);
    }

    private Node makeLeftPanel() {
        VBox leftPanel = new VBox();
        Button goToTopSiteButton = makeButton("Go to selected site", event -> update(topSitesDisplay.getSelectedSite()));
        selectFavoriteButton = makeButton("Go to selected site", event -> update(nanoBrowser.getURLFromReference(addFavoriteDisplay.getSiteToVisit())));
        selectFavoriteButton.setDisable(true);
        leftPanel.getChildren().addAll(topSitesDisplay.getTopSitesDisplay(), goToTopSiteButton, addFavoriteDisplay.getChooseFavoriteSite(), selectFavoriteButton);
        return leftPanel;
    }

    /**
     * Display given URL.
     */
    public void showPage (String url) {
        try {
            update(nanoBrowser.handleNewURL(url));
        }
        catch (Exception e) {
            e.printStackTrace();
            showError(String.format("Could not load %s", url));
        }
    }

    // Update just the view to display given URL
    private void update (URL url) {
        updateBesidesView(url);
        String urlText = url.toString();
        myPage.getEngine().load(urlText);
        myURLDisplay.setText(urlText);
    }

    private void updateBesidesView(URL url) {
        nanoBrowser.incrementURLFrequency(url);
        topSitesDisplay.updateTopSites(nanoBrowser.getUrlsFreqOrdered());
        backButton.setDisable(!nanoBrowser.hasBack());
        nextButton.setDisable(!nanoBrowser.hasNext());
    }

    // Display given message as information in the GUI
    private void showStatus (String message) {
        myStatus.setText(message);
    }

    // Display given message as an error in the GUI
    private void showError (String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Browser Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Make user-entered URL/text field and back/next buttons
    private Node makeInputPanel () {
        HBox result = new HBox();
        // create buttons, with their associated actions
        backButton = makeButton("Back", event -> update(nanoBrowser.back()));
        nextButton = makeButton("Next", event -> update(nanoBrowser.next()));
        // if user presses button or enter in text field, load/show the URL
        EventHandler<ActionEvent> showHandler = event -> showPage(myURLDisplay.getText());
        Button goButton = makeButton("Go", showHandler);
        Button setHomeButton = makeButton("Set Home", event -> nanoBrowser.setHome());
        Button goHomeButton = makeButton("Go Home", event -> {
            try {
                update(nanoBrowser.getHome());
            } catch (NullPointerException e) {
                //FIXME: Handle this better
                e.printStackTrace();
            }
        });
        Button addFavoriteButton = makeButton("Set Favorite", event -> addFavoriteDisplay.setupAddFavoritePopup());
        myURLDisplay = makeInputField(40, showHandler);
        addFavoriteDisplay = new AddFavoriteDisplay(event -> {
            try {
                addFavoriteRefToBrowser();
            } catch (IllegalAccessException e) {
                // TODO: Make better
                e.printStackTrace();
            }
        });
        result.getChildren().addAll(backButton, nextButton, goButton, setHomeButton, goHomeButton, addFavoriteButton, myURLDisplay);
        return result;
    }


    private void addFavoriteRefToBrowser() throws IllegalAccessException {
        selectFavoriteButton.setDisable(false);
        addFavoriteDisplay.updateFavoriteChoices();
        nanoBrowser.addReferenceToMap(addFavoriteDisplay.getInput(), myURLDisplay.getText());
    }

    // Make panel where "would-be" clicked URL is displayed
    private Node makeInformationPanel () {
        // BLANK must be non-empty or status label will not be displayed in GUI
        myStatus = new Label(BLANK);
        return myStatus;
    }

    // Typical code to create HTML page display
    private Node makePageDisplay () {
        myPage = new WebView();
        // catch "browsing" events within web page
        myPage.getEngine().getLoadWorker().stateProperty().addListener(new LinkListener());
        return myPage;
    }

    // Typical code to create button
    private Button makeButton (String label, EventHandler<ActionEvent> handler) {
        Button result = new Button();
        result.setText(label);
        result.setOnAction(handler);
        return result;
    }

    // Typical code to create text field for input
    private TextField makeInputField (int width, EventHandler<ActionEvent> handler) {
        TextField result = new TextField();
        result.setPrefColumnCount(width);
        result.setOnAction(handler);
        return result;
    }

    // Inner class to deal with link-clicks and mouse-overs Mostly taken from
    //   http://blogs.kiyut.com/tonny/2013/07/30/javafx-webview-addhyperlinklistener/
    private class LinkListener implements ChangeListener<State> {
        public static final String ANCHOR = "a";
        public static final String HTML_LINK = "href";
        public static final String EVENT_CLICK = "click";
        public static final String EVENT_MOUSEOVER = "mouseover";
        public static final String EVENT_MOUSEOUT = "mouseout";

        @Override
        public void changed (ObservableValue<? extends State> ov, State oldState, State newState) {
            if (newState == Worker.State.SUCCEEDED) {
                EventListener listener = event -> handleMouse(event);
                Document doc = myPage.getEngine().getDocument();
                NodeList nodes = doc.getElementsByTagName(ANCHOR);
                for (int k = 0; k < nodes.getLength(); k+=1) {
                    EventTarget node = (EventTarget)nodes.item(k);
                    node.addEventListener(EVENT_CLICK, listener, false);
                    node.addEventListener(EVENT_MOUSEOVER, listener, false);
                    node.addEventListener(EVENT_MOUSEOUT, listener, false);
                }
            }
        }

        // Give user feedback as expected by modern browsers
        private void handleMouse (Event event) {
            final String href = ((Element)event.getTarget()).getAttribute(HTML_LINK);
            if (href != null) {
                switch (event.getType()) {
                    case EVENT_CLICK -> showPage(href);
                    case EVENT_MOUSEOVER -> showStatus(href);
                    case EVENT_MOUSEOUT -> showStatus(BLANK);
                }
            }
        }
    }
}
