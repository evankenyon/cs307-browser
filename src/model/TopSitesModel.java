package model;

import java.net.URL;
import java.util.*;

/**
 * Purpose: Represent a model component that maps URLs to how frequently
 * they have been visited and that can return an ordered list of URLs
 * by that frequency
 * Dependencies: URL, java.util
 * Example: Construct a TopSitesModel object to be used in NanoBrowserDisplay
 * in order to handle the backend in regards to recording how frequently sites
 * are being visited and setting the order of the displayed top sites based
 * on frequency
 *
 * @Author Evan Kenyon
 */
public class TopSitesModel {
    private Map<URL, Integer> siteFreq;
    private List<URL> urlsFreqOrdered;

    /**
     * Purpose: Construct a TopSitesModel object by instantiating the instance
     * variables to an empty HashMap and an empty ArrayList
     */
    public TopSitesModel() {
        siteFreq = new HashMap<>();
        urlsFreqOrdered = new ArrayList<>();
    }

    /**
     * Purpose: Increment the value for the url key by 1 and put 0 as the value
     * for a url key before incrementing by 1 if url is not already a key in the
     * siteFreq map
     * Assumptions: Called when user visits url
     * @param url the site that the user is currently visiting
     */
    public void incrementURLFrequency(URL url) {
        if(!siteFreq.containsKey(url)) {
            siteFreq.put(url, 0);
        }
        siteFreq.put(url, siteFreq.get(url) + 1);
    }

    /**
     * Purpose: Get a list of the URLs that the user has visited in order of frequency
     * Assumptions: incrementURLFrequency was used correctly
     * @return List of top 10 URLs in order of frequency
     */
    public List<URL> getUrlsFreqOrdered() {
        sortTopURLs();
        return urlsFreqOrdered.subList(0, Math.min(urlsFreqOrdered.size(), 10));
    }

    private void sortTopURLs() {
        urlsFreqOrdered.clear();
        urlsFreqOrdered.addAll(siteFreq.keySet());
        // Borrowed code for Comparator/sorting from
        // https://stackoverflow.com/questions/18129807/in-java-how-do-you-sort-one-list-based-on-another
        urlsFreqOrdered.sort(new Comparator<URL>() {
            @Override
            public int compare(URL left, URL right) {
                return -Integer.compare(siteFreq.get(left), siteFreq.get(right));
            }
        });
    }
}
