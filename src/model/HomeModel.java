package model;

import java.net.URL;

/**
 * Purpose:
 * Assumptions:
 * Dependencies:
 * Example:
 * Other details:
 *
 * @Author Evan Kenyon
 */
public class HomeModel {
    private URL homeURL;

    /**
     * Purpose:
     * Assumptions:
     * @param currURL
     */
    public void setHome(URL currURL) {
        homeURL = currURL;
    }

    /**
     * Purpose:
     * Assumptions:
     * @return
     * @throws NullPointerException
     */
    // TODO: Change exception type?
    public URL getHome() throws NullPointerException {
        if(homeURL == null) {
            throw new NullPointerException();
        }
        return homeURL;
    }

}
