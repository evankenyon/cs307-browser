package model;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Purpose: Represent a model of a web browser to be used as the backend for NanoBrowserDisplay
 * Assumptions: Methods are used in display class appropriately (i.e. next and back are not called
 * when the user did not actually perform those actions)
 * Dependencies: IOException, MalformedURLException, URL, ArrayList, List
 * Example: Construct a NanoBrowserModel object in NanoBrowserDisplay to serve as the backend,
 * dealing with new URLs, going back/forward, and other basic features
 *
 * @Author Evan Kenyon
 */
public class NanoBrowserModel {
    private URL myCurrentURL;
    private int myCurrentIndex;
    private List<URL> myHistory;
    private HomeModel homeModel;

    /**
     * Purpose: Construct a NanoBrowserModel by instantiating member
     * variables to basic values
     */
    public NanoBrowserModel() {
        myCurrentURL = null;
        myCurrentIndex = -1;
        myHistory = new ArrayList<>();
        homeModel = new HomeModel();
    }

    /**
     * Purpose: Get next URL in history
     * @return next URL in history
     * @throws IndexOutOfBoundsException thrown if there is no next URL in history
     * (i.e. if user is at most recent URL)
     */
    public URL next() throws IndexOutOfBoundsException{
        myCurrentIndex += 1;
        if(myCurrentIndex >= myHistory.size()) {
            throw new IndexOutOfBoundsException();
        }
        return myHistory.get(myCurrentIndex);
    }

    /**
     * Purpose: Get previous URL in history
     * @return previous URL in history
     * @throws IndexOutOfBoundsException thorwn if there is no previous URL in history
     * (i.e. user is at first URL in history)
     */
    // Move to previous URL in the history
    public URL back () throws IndexOutOfBoundsException{
        myCurrentIndex -= 1;
        if(myCurrentIndex < 0) {
            throw new IndexOutOfBoundsException();
        }
        return myHistory.get(myCurrentIndex);
    }

    /**
     * Purpose: Uses a HomeModel object to set the home URL to
     * myCurrentURL
     * Assumptions: homeModel is properly instantiated
     */
    public void setHome() {
        homeModel.setHome(myCurrentURL);
    }

    /**
     * Purpose: Get the home URL using a HomeModel object
     * Assumptions: homeModel is properly instantiated
     * @return the home URL
     * @throws NullPointerException thrown if homeURL has not been set
     */
    public URL getHome() throws NullPointerException{
        URL homeURL = homeModel.getHome();
        addURLToHistory(homeURL);
        return homeURL;
    }

    /**
     * Purpose: Handle user going to a new URL by constructing a URL
     * using url argument, making sure its successful, adding it to history,
     * and returning it for display to show page corresponding to the URL
     * @param url url to visit/String to use in URL constructor
     * @return URL object corresponding to url String argument
     * @throws IOException thrown if internal completeURL function does not
     * return a valid URL
     */
    public URL handleNewURL(String url) throws IOException {
        URL tmp = completeURL(url);
        if (tmp != null) {
            // unfortunately, completeURL may not have returned a valid URL, so test it
            tmp.openStream();
            // if successful, remember this URL
            myCurrentURL = tmp;
            addURLToHistory(myCurrentURL);
            return myCurrentURL;
        }
        throw new IOException();
    }

    private void addURLToHistory(URL url) {
        if (hasNext()) {
            myHistory = myHistory.subList(0, myCurrentIndex + 1);
        }
        myHistory.add(url);
        myCurrentIndex += 1;
    }

    /**
     * Purpose (comment borrowed from Prof. Duvall): Returns true if there is a next URL available
     * @return true if there is a next URL available
     */
    public boolean hasNext () {
        return myCurrentIndex < (myHistory.size() - 1);
    }

    /**
     * Purpose: Returns true if there is a previous URL available
     * @return true if there is a previous URL available
     */
    public boolean hasBack () {
        return myCurrentIndex > 0;
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
}
