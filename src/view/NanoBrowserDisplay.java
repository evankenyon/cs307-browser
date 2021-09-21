package view;

import java.net.MalformedURLException;
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
import model.FavoritesModel;
import model.NanoBrowserModel;
import model.TopSitesModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import util.ButtonMaker;


/**
 * Purpose (comment borrowed from Prof. Duvall): A class used to display the viewer for a simple HTML browser.
 * Dependencies: JavaFX, URL, MalformedURLException, model package, Document, Element, NodeList, Event, EventListener,
 * EventTarget, ButtonMaker
 * Example: Instantiate a NanoBrowserDisplay object in a main class in order to have a simple web browser for usage
 * See this tutorial for help on how to use all variety of components:
 *   http://download.oracle.com/otndocs/products/javafx/2/samples/Ensemble/
 * 
 * @author Owen Astrachan
 * @author Marcin Dobosz
 * @author Yuzhang Han
 * @author Edwin Ward
 * @author Robert C. Duvall
 * @author Evan Kenyon
 */
public class NanoBrowserDisplay {
    public static final String BLANK = " ";

    private WebView myPage;
    private TextField myURLDisplay;
    private Label myStatus;
    private NanoBrowserModel nanoBrowserModel;
    private FavoritesModel favoritesModel;
    private TopSitesModel topSitesModel;
    private FavoritesDisplay favoritesDisplay;
    private TopSitesDisplay topSitesDisplay;
    private HomeDisplay homeDisplay;
    private Button nextButton;
    private Button backButton;
    private Button goButton;


    /**
     * Purpose (comment borrowed from Prof. Duvall): Create a web browser with prompts in the given language with initially empty state.
     */
    public NanoBrowserDisplay() {
        nanoBrowserModel = new NanoBrowserModel();
        topSitesModel = new TopSitesModel();
        favoritesModel = new FavoritesModel();
        topSitesDisplay = new TopSitesDisplay(event -> update(topSitesDisplay.getSelectedSite()));
        setupAddFavoriteDisplay();
    }

    /**
     * Purpose (comment borrowed from Prof. Duvall):  Returns scene for the browser, so it can be added to stage.
     */
    public Scene makeScene (int width, int height) {
        BorderPane root = new BorderPane();
        // must be first since other panels may refer to page
        root.setCenter(makePageDisplay());
        root.setTop(makeInputPanel());
        root.setBottom(makeInformationPanel());
        root.setLeft(makeLeftPanel());
        return new Scene(root, width, height);
    }

    /**
     * Purpose (comment borrowed from Prof. Duvall): Display given URL.
     */
    public void showPage (String url) {
        try {
            update(nanoBrowserModel.handleNewURL(url));
        }
        catch (Exception e) {
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
        topSitesModel.incrementURLFrequency(url);
        topSitesDisplay.updateTopSites(topSitesModel.getUrlsFreqOrdered());
        backButton.setDisable(!nanoBrowserModel.hasBack());
        nextButton.setDisable(!nanoBrowserModel.hasNext());
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

    private void setupAddFavoriteDisplay() {
        favoritesDisplay = new FavoritesDisplay(event -> {
            try {
                update(favoritesModel.getURLFromReference(favoritesDisplay.getSiteToVisit()));
            } catch (IllegalAccessException e) {
                showError("Cannot go to a favorite site without selecting one");
            }
        }, event -> {
            try {
                favoritesDisplay.addFavoriteRefToBrowser(favoritesModel, myURLDisplay);
            } catch (IllegalAccessException e) {
                showError("Cannot go to a favorite site without selecting one");
            } catch (MalformedURLException e) {
                showError(String.format("%s is not a valid URL", myURLDisplay.getText()));
            }
        });
    }

    private Node makeLeftPanel() {
        VBox leftPanel = new VBox();
        leftPanel.getChildren().addAll(topSitesDisplay.getTopSitesDisplay(), favoritesDisplay.getDisplayComponents());
        return leftPanel;
    }

    // Make user-entered URL/text field and back/next buttons
    private Node makeInputPanel () {
        setupHistoryButtons();
        setupShowHandlerDependentNodes();
        setupHomeDisplay();
        return new HBox(backButton, nextButton, goButton, homeDisplay.getHomeDisplay(), myURLDisplay);
    }

    private void setupHomeDisplay() {
        homeDisplay = new HomeDisplay(event -> {
            nanoBrowserModel.setHome();
            homeDisplay.enableGoHomeButton();
        }, event -> update(nanoBrowserModel.getHome()));
    }

    private void setupHistoryButtons() {
        backButton = ButtonMaker.makeButton("Back", event -> update(nanoBrowserModel.back()));
        nextButton = ButtonMaker.makeButton("Next", event -> update(nanoBrowserModel.next()));
    }

    private void setupShowHandlerDependentNodes() {
        // if user presses button or enter in text field, load/show the URL
        EventHandler<ActionEvent> showHandler = event -> showPage(myURLDisplay.getText());
        goButton = ButtonMaker.makeButton("Go", showHandler);
        myURLDisplay = makeInputField(40, showHandler);
        myURLDisplay.textProperty().addListener(event -> {
            goButton.setDisable(myURLDisplay.getText().equals(""));
        });
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
