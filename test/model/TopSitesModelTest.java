package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TopSitesModelTest {
    private TopSitesModel topSitesModel;
    private URL testURLOne;
    private URL testURLTwo;

    @BeforeEach
    void setUp() throws MalformedURLException {
        topSitesModel = new TopSitesModel();
        testURLOne = new URL("http://google.com");
        testURLTwo = new URL("http://yahoo.com");
    }

    @Test
    void testGetUrlsFreqOrdered() {
        topSitesModel.incrementURLFrequency(testURLOne);
        topSitesModel.incrementURLFrequency(testURLOne);
        topSitesModel.incrementURLFrequency(testURLTwo);
        List<URL> orderedURLs = topSitesModel.getUrlsFreqOrdered();
        assertEquals(orderedURLs.get(0), testURLOne);
        assertEquals(orderedURLs.get(1), testURLTwo);
    }
}