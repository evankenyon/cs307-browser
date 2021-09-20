package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TopSitesTest {
    private TopSites topSites;
    private URL testURLOne;
    private URL testURLTwo;

    @BeforeEach
    void setUp() throws MalformedURLException {
        topSites = new TopSites();
        testURLOne = new URL("http://google.com");
        testURLTwo = new URL("http://yahoo.com");
    }

    @Test
    void testGetUrlsFreqOrdered() {
        topSites.incrementURLFrequency(testURLOne);
        topSites.incrementURLFrequency(testURLOne);
        topSites.incrementURLFrequency(testURLTwo);
        List<URL> orderedURLs = topSites.getUrlsFreqOrdered();
        assertEquals(orderedURLs.get(0), testURLOne);
        assertEquals(orderedURLs.get(1), testURLTwo);
    }
}