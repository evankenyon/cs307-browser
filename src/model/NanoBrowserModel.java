package model;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
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
public class NanoBrowserModel {
    private URL myCurrentURL;
    private int myCurrentIndex;
    private List<URL> myHistory;
    private HomeModel homeModel;

    /**
     * Purpose:
     * Assumptions:
     */
    public NanoBrowserModel() {
        myCurrentURL = null;
        myCurrentIndex = -1;
        myHistory = new ArrayList<>();
        homeModel = new HomeModel();
    }

    /**
     * Purpose:
     * Assumptions:
     * @return
     * @throws IndexOutOfBoundsException
     */
    // Move to next URL in the history
    public URL next() throws IndexOutOfBoundsException{
        myCurrentIndex += 1;
        if(myCurrentIndex >= myHistory.size()) {
            throw new IndexOutOfBoundsException();
        }
        return myHistory.get(myCurrentIndex);
    }

    /**
     * Purpose:
     * Assumptions:
     * @return
     * @throws IndexOutOfBoundsException
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
     * Purpose:
     * Assumptions:
     * @throws MalformedURLException
     */
    public void setHome() throws MalformedURLException {
        homeModel.setHome(myCurrentURL);
    }

    /**
     * Purpose:
     * Assumptions:
     * @return
     * @throws NullPointerException
     */
    // TODO: Change exception type?
    public URL getHome() throws NullPointerException{
        URL homeURL = homeModel.getHome();
        addURLToHistory(homeURL);
        return homeURL;
    }

    /**
     * Purpose:
     * Assumptions:
     * @param url
     * @return
     * @throws IOException
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

    // Returns true if there is a next URL available
    public boolean hasNext () {
        return myCurrentIndex < (myHistory.size() - 1);
    }

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
