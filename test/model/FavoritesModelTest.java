package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.*;

class FavoritesModelTest {
    private FavoritesModel favoritesModel;
    private String testURLOne;

    @BeforeEach
    public void setUp() {
      favoritesModel = new FavoritesModel();
      testURLOne = "http://google.com";
    }

    @Test
    public void correctClassUsage() throws MalformedURLException {
        favoritesModel.addReferenceToMap("test", testURLOne);
        assertEquals(testURLOne, favoritesModel.getURLFromReference("test").toString());
    }
}