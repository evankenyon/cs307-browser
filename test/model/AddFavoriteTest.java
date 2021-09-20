package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.*;

class AddFavoriteTest {
    private AddFavorite addFavorite;
    private String testURLOne;

    @BeforeEach
    void setUp() {
      addFavorite = new AddFavorite();
      testURLOne = "http://google.com";
    }

    @Test
    void correctClassUsage() throws MalformedURLException {
        addFavorite.addReferenceToMap("test", testURLOne);
        assertEquals(testURLOne, addFavorite.getURLFromReference("test").toString());
    }
}