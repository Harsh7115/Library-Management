// Author: Ashish Dev Choudhary (ashishdev13) & Harshit Jain (hjain)
/**
  Library class represents a collection of books and provides various methods 
  to manage them. It includes functionalities such as adding books, searching 
  by title, author, or rating, marking books as read, rating books, and much more.
  
  In this class, encapsulation is maintained by making the list of books, i.e. books
  private and only exposing methods for interacting with the books. This ensures 
  the integrity of the data by restricting direct access to the nooks list.
 */

import java.util.*;
import java.io.*;

public class Library {
    private List<Book> books;

    /**
      Constructor that initializes an empty list of books.
    */
    public Library() {
        books = new ArrayList<>();
    }

    /**
      Adds a new book to the list.
      @param book The book to be added.
    */
    public void addBook(Book book) {
        books.add(book);
    }

    /**
      Searches for books by title, returning a list of matching books.
      @param title The title of the book to search for.
      @return A list of books that match the title.
    */
    public List<Book> searchByTitle(String title) {
        List<Book> results = new ArrayList<>();
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {  // Case-insensitive comparison.
                results.add(book);
            }
        }
        return results;
    }

    /**
      Searches for books by author, returning a list of matching books.
      @param author The author of the book to search for.
      @return A list of books that match the author.
    */
    public List<Book> searchByAuthor(String author) {
        List<Book> results = new ArrayList<>();
        for (Book book : books) {
            if (book.getAuthor().equalsIgnoreCase(author)) {  // Case-insensitive comparison.
                results.add(book);
            }
        }
        return results;
    }

    /**
      Searches for books by rating, returning a list of books with the specified rating.
      @param rating The rating to search for.
      @return A list of books with the specified rating.
    */
    public List<Book> searchByRating(int rating) {
        List<Book> results = new ArrayList<>();
        for (Book book : books) {
            if (book.getRating() == rating) {  // Matches books with the same rating.
                results.add(book);
            }
        }
        return results;
    }

    /**
      Marks a book as read based on its title.
      @param title The title of the book to mark as read.
     * @return 
    */
    public boolean setToRead(String title) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                book.setRead(true); 
                System.out.println(book.getTitle() + " marked as read.");
                return true;
            }
        }
        
        System.out.println("Book not found.");
        return false;
    }

    /**
      Rates a book based on its title and the provided rating.
      @param title The title of the book to rate.
      @param rating The rating to assign to the book (1-5).
     * @return 
    */
    public boolean rateBook(String title, int rating) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                book.setRating(rating);
                System.out.println(book.getTitle() + " rated " + rating);
                return true;
            }
        }
        System.out.println("Book not found.");
        return false;
    }

    /**
      Retrieves all books sorted by title.
      @return A list of books sorted alphabetically by title.
    */
    public List<Book> getAllBooksSortedByTitle() {
        List<Book> sortedBooks = new ArrayList<>(books);
        
        // Bubble sort algorithm to sort books by title
        for (int i = 0; i < sortedBooks.size() - 1; i++) {
            for (int j = 0; j < sortedBooks.size() - i - 1; j++) {
                if (sortedBooks.get(j).getTitle().compareTo(sortedBooks.get(j + 1).getTitle()) > 0) {
                    Book temp = sortedBooks.get(j);
                    sortedBooks.set(j, sortedBooks.get(j + 1));
                    sortedBooks.set(j + 1, temp);
                }
            }
        }
        return sortedBooks;
    }

    /**
      Retrieves all books sorted by author.
      @return A list of books sorted alphabetically by author.
    */
    public List<Book> getAllBooksSortedByAuthor() {
        List<Book> sortedBooks = new ArrayList<>(books);
        
        // Bubble sort algorithm to sort books by author
        for (int i = 0; i < sortedBooks.size() - 1; i++) {
            for (int j = 0; j < sortedBooks.size() - i - 1; j++) {
                if (sortedBooks.get(j).getAuthor().compareTo(sortedBooks.get(j + 1).getAuthor()) > 0) {
                    Book temp = sortedBooks.get(j);
                    sortedBooks.set(j, sortedBooks.get(j + 1));
                    sortedBooks.set(j + 1, temp);
                }
            }
        }
        return sortedBooks;
    }


    /**
      Retrieves all books that have been marked as read.
      @return A list of books that are marked as read.
    */
    public List<Book> getReadBooks() {
        List<Book> readBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.isRead()) {  
                readBooks.add(book);
            }
        }
        return readBooks;
    }

    /**
      Retrieves all books that have not been marked as read.
      @return A list of books that are unread.
    */
    public List<Book> getUnreadBooks() {
        List<Book> unreadBooks = new ArrayList<>();
        for (Book book : books) {
            if (!book.isRead()) {  
                unreadBooks.add(book);
            }
        }
        return unreadBooks;  
    }

    /**
      Suggests a random unread book from the library.
      @return A randomly selected unread book, or null if no unread books exist.
    */
    public Book suggestRandomUnreadBook() {
        List<Book> unreadBooks = getUnreadBooks();
        if (unreadBooks.isEmpty()) {
            return null;  
        }
        Random random = new Random();
        return unreadBooks.get(random.nextInt(unreadBooks.size()));
    }

    /**
      Adds multiple books from a file where each line represents a book.
      Each line should be in the format: title;author
      @param filename The name of the file to read books from.
    */
    public void addBooksFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");  
                String title = parts[0];
                String author = parts[1];
                int rating = 0;
                addBook(new Book(title, author, rating));
            }
            System.out.println("Books added from file.");
        } catch (IOException e) {
            System.out.println("Error reading the file.");
        }
    }
}
 