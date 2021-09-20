package model;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Purpose: Represent a model component that maps a string to a URL
 * so that a user can save URLs as favorites
 * Dependencies: MalformedURLException, URL, HashMap, Map
 * Example: Construct a favorite model object to be used in NanoBrowserDisplay
 * in order to set up the backend for a favorite site selection display (FavoritesDisplay)
 *
 * @Author Evan Kenyon
 */
public class FavoritesModel {
    private Map<String, URL> nameToURL;

    /**
     * Purpose: Construct a FavoritesModel class by instantiating the nameToURL instance variable
     */
    public FavoritesModel() {
        nameToURL = new HashMap<>();
    }

    /**
     * Purpose: Add a reference (String that represents a URL) to the nameToURL map
     * @param ref nonempty String that references the url argument
     * @param url String that can be used in the URL constructor, otherwise a MalformedURLException is thrown
     * @throws MalformedURLException thrown if url argument is malformed
     */
    public void addReferenceToMap(String ref, String url) throws MalformedURLException {
        nameToURL.put(ref, new URL(url));
    }

    /**
     * Purpose: Get the URL that is mapped to by ref
     * @param ref key that maps to the desired URL
     * @return the URL that is mapped to by ref
     * @throws NullPointerException thrown if ref key is not in nameToURL map
     */
    public URL getURLFromReference(String ref) throws NullPointerException{
        if(!nameToURL.containsKey(ref)) {
            throw new NullPointerException();
        }
        return nameToURL.get(ref);
    }
}
