package model;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NanoBrowser {
    private URL myCurrentURL;
    private int myCurrentIndex;
    private List<URL> myHistory;
    private Home home;

    public NanoBrowser() {
        myCurrentURL = null;
        myCurrentIndex = -1;
        myHistory = new ArrayList<>();
        home = new Home();
    }

    // Move to next URL in the history
    public URL next() throws IndexOutOfBoundsException{
        myCurrentIndex += 1;
        if(myCurrentIndex >= myHistory.size()) {
            throw new IndexOutOfBoundsException();
        }
        return myHistory.get(myCurrentIndex);
    }

    // Move to previous URL in the history
    public URL back () throws IndexOutOfBoundsException{
        myCurrentIndex -= 1;
        if(myCurrentIndex < 0) {
            throw new IndexOutOfBoundsException();
        }
        return myHistory.get(myCurrentIndex);
    }

    public void setHome() throws MalformedURLException {
        home.setHome(myCurrentURL);
    }

    // TODO: Change exception type?
    public URL getHome() throws NullPointerException{
        URL homeURL = home.getHome();
        addURLToHistory(homeURL);
        return homeURL;
    }

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
