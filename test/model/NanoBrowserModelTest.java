package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

public class NanoBrowserModelTest {
    private NanoBrowserModel nanoBrowser;
    private String testURLOne;
    private String testURLTwo;

    @BeforeEach
    public void setUp() {
        nanoBrowser = new NanoBrowserModel();
        testURLOne = "http://google.com";
        testURLTwo = "http://yahoo.com";
    }

    @Test
    public void testNextCorrectUsage() throws IOException {
        nanoBrowser.handleNewURL(testURLTwo);
        nanoBrowser.handleNewURL(testURLOne);
        nanoBrowser.back();
        assertEquals(nanoBrowser.next(), new URL(testURLOne));
    }

    @Test
    public void testNextIncorrectUsage() throws IOException{
        nanoBrowser.handleNewURL(testURLOne);
        assertThrows(IndexOutOfBoundsException.class, () -> nanoBrowser.next());
    }

    @Test
    public void testBackCorrectUsage() throws IOException {
        nanoBrowser.handleNewURL(testURLOne);
        nanoBrowser.handleNewURL(testURLTwo);
        assertEquals(nanoBrowser.back(), new URL(testURLOne));
    }

    @Test
    public void testBackIncorrectUsage() throws IOException {
        nanoBrowser.handleNewURL(testURLOne);
        assertThrows(IndexOutOfBoundsException.class, () -> nanoBrowser.back());
    }

    @Test
    public void testGetHomeCorrectUsage() throws IOException {
        nanoBrowser.handleNewURL(testURLOne);
        nanoBrowser.setHome();
        assertEquals(nanoBrowser.getHome(), testURLOne);
        assertEquals(nanoBrowser.back(), testURLOne);
    }

    @Test
    public void testHandleNewURLCorrectUsage() throws IOException {
        assertEquals(nanoBrowser.handleNewURL("google.com"), new URL(testURLOne));
    }

    @Test
    public void testHandleNewURLIncorrectUsage() {
        assertThrows(IOException.class, () -> nanoBrowser.handleNewURL("f"));
    }

    @Test
    public void testHasNextFalse() throws IOException {
        nanoBrowser.handleNewURL(testURLOne);
        assertEquals(nanoBrowser.hasNext(), false);
    }

    @Test
    public void testHasNextTrue() throws IOException {
        nanoBrowser.handleNewURL(testURLOne);
        nanoBrowser.handleNewURL(testURLTwo);
        nanoBrowser.back();
        assertEquals(nanoBrowser.hasNext(), true);
    }

    @Test
    public void testHasBackFalse() throws IOException {
        nanoBrowser.handleNewURL(testURLOne);
        assertEquals(nanoBrowser.hasBack(), false);
    }

    @Test
    public void testHasBackTrue() throws IOException {
        nanoBrowser.handleNewURL(testURLOne);
        nanoBrowser.handleNewURL(testURLTwo);
        assertEquals(nanoBrowser.hasBack(), true);
    }
}