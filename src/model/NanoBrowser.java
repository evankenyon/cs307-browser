package model;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NanoBrowser {
    private URL myCurrentURL;
    private URL homeURL;
    private int myCurrentIndex;
    private List<URL> myHistory;
    private AddFavorite addFavorite;
    private TopSites topSites;


    public NanoBrowser() {
        myCurrentURL = null;
        homeURL = null;
        myCurrentIndex = -1;
        myHistory = new ArrayList<>();
        addFavorite = new AddFavorite();
        topSites = new TopSites();
    }

    // Move to next URL in the history
    public URL next () {
        myCurrentIndex += 1;
        URL nextUrl = myHistory.get(myCurrentIndex);
        incrementURLFrequency(nextUrl);
        return nextUrl;
    }

    // Move to previous URL in the history
    public URL back () {
        myCurrentIndex -= 1;
        URL prevUrl = myHistory.get(myCurrentIndex);
        incrementURLFrequency(prevUrl);
        return prevUrl;
    }

    public void setHome() {
        homeURL = myCurrentURL;
    }

    // TODO: Change exception type?
    public URL getHome() throws NullPointerException {
        addURLToHistory(homeURL);
        if(homeURL == null) {
            throw new NullPointerException();
        }
        return homeURL;
    }

    public URL handleNewURL(String url) throws IOException {
        URL tmp;
        if(addFavorite.doesReferenceHaveURL(url)) {
            tmp = addFavorite.getURLFromReference(url);
        } else {
            tmp = completeURL(url);
        }
        if (tmp != null) {
            // unfortunately, completeURL may not have returned a valid URL, so test it
            tmp.openStream();
            // if successful, remember this URL
            myCurrentURL = tmp;
            addURLToHistory(myCurrentURL);
            topSites.incrementURLFrequency(myCurrentURL);
            return myCurrentURL;
        }
        throw new IOException();
    }

    public void addReferenceToMap(String ref, String url) {
        addFavorite.addReferenceToMap(ref, completeURL(url));
    }

    private void incrementURLFrequency(URL url) {
        topSites.incrementURLFrequency(url);
    }

    public List<URL> getUrlsFreqOrdered() {
        return topSites.getUrlsFreqOrdered();
    }

    private void addURLToHistory(URL url) {
        if (hasNext()) {
            myHistory = myHistory.subList(0, myCurrentIndex + 1);
        }
        myHistory.add(url);
        incrementURLFrequency(url);
        myCurrentIndex += 1;
    }

    // Deal with a potentially incomplete URL
    public URL completeURL (String possible) {
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

    // Returns true if there is a next URL available
    private boolean hasNext () {
        return myCurrentIndex < (myHistory.size() - 1);
    }
}
