package model;

import java.net.MalformedURLException;
import java.net.URL;

public class Home {
    private URL homeURL;

    public void setHome(URL currURL) {
        homeURL = currURL;
    }

    // TODO: Change exception type?
    public URL getHome() throws NullPointerException {
        if(homeURL == null) {
            throw new NullPointerException();
        }
        return homeURL;
    }

}
