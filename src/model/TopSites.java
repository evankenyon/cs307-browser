package model;

import java.net.URL;
import java.util.*;

public class TopSites {
    private Map<URL, Integer> siteFreq;
    private List<URL> urlsFreqOrdered;

    public TopSites() {
        siteFreq = new HashMap<>();
        urlsFreqOrdered = new ArrayList<>();
    }

    public void incrementURLFrequency(URL url) {
        if(!siteFreq.containsKey(url)) {
            siteFreq.put(url, 0);
        }
        siteFreq.put(url, siteFreq.get(url) + 1);
    }

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
