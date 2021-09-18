package browser;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;
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
public class NanoBrowser {
    // constants
    public static final String BLANK = " ";

    // web page
    private WebView myPage;
    // navigation
    private TextField myURLDisplay;
    // information area
    private Label myStatus;
    // data
    private URL myCurrentURL;
    private int myCurrentIndex;
    private List<URL> myHistory;


    /**
     * Create a web browser with prompts in the given language with initially empty state.
     */
    public NanoBrowser () {
        myCurrentURL = null;
        myCurrentIndex = -1;
        myHistory = new ArrayList<>();
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
        // create scene to hold UI
        return new Scene(root, width, height);
    }

    /**
     * Display given URL.
     */
    public void showPage (String url) {
        try {
            URL tmp = completeURL(url);
            if (tmp != null) {
                // unfortunately, completeURL may not have returned a valid URL, so test it
                tmp.openStream();
                // if successful, remember this URL
                myCurrentURL = tmp;
                if (hasNext()) {
                    myHistory = myHistory.subList(0, myCurrentIndex + 1);
                }
                myHistory.add(myCurrentURL);
                myCurrentIndex += 1;
                update(myCurrentURL);
            }
        }
        catch (Exception e) {
            showError(String.format("Could not load %s", url));
        }
    }

    // Move to next URL in the history
    private void next () {
        myCurrentIndex += 1;
        update(myHistory.get(myCurrentIndex));
    }

    // Move to previous URL in the history
    private void back () {
        myCurrentIndex -= 1;
        update(myHistory.get(myCurrentIndex));
    }

    // Returns true if there is a next URL available
    private boolean hasNext () {
        return myCurrentIndex < (myHistory.size() - 1);
    }

    // Update just the view to display given URL
    private void update (URL url) {
        String urlText = url.toString();
        myPage.getEngine().load(urlText);
        myURLDisplay.setText(urlText);
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
        Button backButton = makeButton("Back", event -> back());
        result.getChildren().add(backButton);
        Button nextButton = makeButton("Next", event -> next());
        result.getChildren().add(nextButton);
        // if user presses button or enter in text field, load/show the URL
        EventHandler<ActionEvent> showHandler = event -> showPage(myURLDisplay.getText());
        result.getChildren().add(makeButton("Go", showHandler));
        myURLDisplay = makeInputField(40, showHandler);
        result.getChildren().add(myURLDisplay);
        return result;
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

    // Deal with a potentially incomplete URL
    private URL completeURL (String possible) {
        final String PROTOCOL_PREFIX = "http://";
        try {
            // try it as is
            return new URL(possible);
        }
        catch (MalformedURLException e) {
            try {
                // e.g., let user leave off initial protocol
                return new URL(PROTOCOL_PREFIX + possible);
            }
            catch (MalformedURLException ee) {
                try {
                    // try it as a relative link
                    // FIXME: need to generalize this :(
                    return new URL(myCurrentURL.toString() + "/" + possible);
                }
                catch (MalformedURLException eee) {
                    // FIXME: not a good way to handle an error!
                    e.printStackTrace();
                    return null;
                }
            }
        }
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
