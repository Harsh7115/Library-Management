import org.junit.jupiter.api.BeforeEach; 
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

/*
 * File: LibraryTest.java
 * Description: Unit tests for the Library class using JUnit 5.
 * Author: Ashish Dev Choudhary (ashishdev13) & Harshit Jain (hjain)
 */

public class LibraryTest {
    private Library library;
    private Book book1;
    private Book book2;

    @BeforeEach
    public void setUp() {
        library = new Library();
        book1 = new Book("The Hobbit", "J.R.R. Tolkien", 5);
        book2 = new Book("To Kill a Mockingbird", "Harper Lee", 4);
        library.addBook(book1);
        library.addBook(book2);
    }

    @Test
    public void testAddBook() {
        Book newBook = new Book("1984", "George Orwell", 5);
        library.addBook(newBook);
        assertTrue(library.searchByTitle("1984").contains(newBook));
    }

    @Test
    public void testSearchByTitle() {
        List<Book> results = library.searchByTitle("The Hobbit");
        assertEquals(1, results.size());
        assertEquals(book1, results.get(0));
    }

    @Test
    public void testSearchByAuthor() {
        List<Book> results = library.searchByAuthor("Harper Lee");
        assertEquals(1, results.size());
        assertEquals(book2, results.get(0));
    }

    @Test
    public void testSearchByRating() {
        List<Book> results = library.searchByRating(5);
        assertEquals(1, results.size());
        assertEquals(book1, results.get(0));
    }

    @Test
    public void testSetToRead() {
        library.setToRead("The Hobbit");
        assertTrue(book1.isRead());
    }

    @Test
    public void testRateBook() {
        library.rateBook("The Hobbit", 3);
        assertEquals(3, book1.getRating());
    }

    @Test
    public void testGetAllBooksSortedByTitle() {
        List<Book> sortedBooks = library.getAllBooksSortedByTitle();
        assertEquals("The Hobbit", sortedBooks.get(0).getTitle());
    }

    @Test
    public void testGetAllBooksSortedByAuthor() {
        List<Book> sortedBooks = library.getAllBooksSortedByAuthor();
        assertEquals("Harper Lee", sortedBooks.get(0).getAuthor());
    }

    @Test
    public void testGetReadBooks() {
        library.setToRead("The Hobbit");
        assertTrue(library.getReadBooks().contains(book1));
    }

    @Test
    public void testGetUnreadBooks() {
        assertTrue(library.getUnreadBooks().contains(book1));
    }

    @Test
    public void testSuggestRandomUnreadBook() {
        assertNotNull(library.suggestRandomUnreadBook());
    }
    
    @Test
    public void testSetToReadBookNotFound() {
        assertFalse(library.setToRead("Non-Existent Book"));
    }
    
    @Test
    public void testRateBookNotFound() {
        assertFalse(library.rateBook("Non-Existent Book", 4));
    }

    @Test
    public void testSuggestRandomUnreadBookWhenNoneExist() {
        library.setToRead("The Hobbit");
        library.setToRead("To Kill a Mockingbird");
        assertNull(library.suggestRandomUnreadBook());
    }

    @Test
    public void testAddBooksFromFileInvalid() {
        library.addBooksFromFile("non_existent_file.txt");
        assertEquals(2, library.getUnreadBooks().size());
    }
    
    @Test
    public void testGetAllBooksSortedByTitleMultipleBooks() {
        Book book3 = new Book("A Tale of Two Cities", "Charles Dickens", 4);
        library.addBook(book3);
        List<Book> sortedBooks = library.getAllBooksSortedByTitle();
        
        assertEquals("A Tale of Two Cities", sortedBooks.get(0).getTitle());
        assertEquals("The Hobbit", sortedBooks.get(1).getTitle());
        assertEquals("To Kill a Mockingbird", sortedBooks.get(2).getTitle());
    }
    

}
