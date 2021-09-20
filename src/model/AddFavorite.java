package model;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Purpose:
 * Assumptions:
 * Dependencies:
 * Example:
 * Other details:
 *
 * @Author Evan Kenyon
 */
public class AddFavorite {
    private Map<String, URL> nameToURL;

    /**
     * Purpose:
     * Assumptions:
     */
    public AddFavorite() {
        nameToURL = new HashMap<>();
    }

    /**
     * Purpose:
     * Assumptions:
     * @param ref
     * @param url
     * @throws MalformedURLException
     */
    public void addReferenceToMap(String ref, String url) throws MalformedURLException {
        nameToURL.put(ref, new URL(url));
    }

    /**
     * Purpose:
     * Assumptions:
     * @param ref
     * @return
     */
    public URL getURLFromReference(String ref) {
        return nameToURL.get(ref);
    }
}
