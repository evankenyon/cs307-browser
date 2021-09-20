package model;

import java.net.URL;

/**
 * Purpose: Represent a model component that saves/sets a homeURL,
 * and returns it when the user wants to go to that URL
 * Dependencies: URL
 * Example: Construct a HomeModel object in NanoBrowserDisplay in order
 * to handle the logic for when set and get home buttons are pressed
 *
 * @Author Evan Kenyon
 */
public class HomeModel {
    private URL homeURL;

    /**
     * Purpose: Construct a HomeModel object with homeURL = null
     */
    public HomeModel() {
        homeURL = null;
    }

    /**
     * Purpose: Set the home URL to currURL
     * Assumptions: currURL is the current URL that
     * the user is on in the browser
     * @param currURL the URL to set homeURL to
     */
    public void setHome(URL currURL) {
        homeURL = currURL;
    }

    /**
     * Purpose: Get the home URL
     * @return the home URL
     * @throws NullPointerException if homeURL was not set before this function
     * is called
     */
    public URL getHome() throws NullPointerException {
        if(homeURL == null) {
            throw new NullPointerException();
        }
        return homeURL;
    }

}
