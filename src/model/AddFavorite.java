package model;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AddFavorite {
    private Map<String, URL> nameToURL;

    public AddFavorite() {
        nameToURL = new HashMap<>();
    }

    public void addReferenceToMap(String ref, String url) throws MalformedURLException {
        nameToURL.put(ref, new URL(url));
    }

    public boolean doesReferenceHaveURL(String ref) {
        return nameToURL.containsKey(ref);
    }

    public URL getURLFromReference(String ref) {
        return nameToURL.get(ref);
    }
}
