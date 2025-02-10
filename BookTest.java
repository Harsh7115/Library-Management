import org.junit.jupiter.api.BeforeEach;  
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/*
 * File: BookTest.java
 * Description: Unit tests for the Book class using JUnit 5.
 * Author: Ashish Dev Choudhary (ashishdev13) & Harshit Jain (hjain)
 */
public class BookTest {
    private Book book;

    @BeforeEach
    public void setUp() {
        book = new Book("The Hobbit", "J.R.R. Tolkien", 5);
    }

    @Test
    public void testGetTitle() {
        assertEquals("The Hobbit", book.getTitle());
    }

    @Test
    public void testGetAuthor() {
        assertEquals("J.R.R. Tolkien", book.getAuthor());
    }

    @Test
    public void testGetRating() {
        assertEquals(5, book.getRating());
    }

    @Test
    public void testSetRating() {
        book.setRating(4);
        assertEquals(4, book.getRating());
    }

    @Test
    public void testIsReadDefaultFalse() {
        assertFalse(book.isRead());
    }

    @Test
    public void testSetRead() {
        book.setRead(true);
        assertTrue(book.isRead());
    }

    @Test
    public void testToString() {
        String expectedOutput = "The Hobbit by J.R.R. Tolkien, Rating: 5 (Unread)";
        assertEquals(expectedOutput, book.toString());
    }
}
