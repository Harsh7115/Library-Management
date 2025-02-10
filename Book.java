// Author: Ashish Dev Choudhary (ashishdev13) & Harshit Jain (hjain) 
/*
The Book class represents a book with a title, author, rating, and read status.
This is encapsulated, because all the fields are private and public getter and setter
methods are there for those. This means that internal data is encapsulated because the access
cannot be changed from outside the class without its permission.
*/
public class Book {
    private final String title;
    private final String author;
    private int rating;
    private boolean read;

    /**
     * Constructs a Book object with the specified title, author, and rating.
     * By default, the book is marked as unread.
     *
     * @param title   the title of the book
     * @param author  the author of the book
     * @param rating  the rating of the book (1-5)
     */
    public Book(String title, String author, int rating) {
        this.title = title;
        this.author = author;
        this.rating = rating;
        this.read = false;
    }

    /**
     * Gets the title of the book.
     *
     * @return the title of the book
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the author of the book.
     *
     * @return the author of the book
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Gets the rating of the book.
     *
     * @return the rating of the book
     */
    public int getRating() {
        return rating;
    }

    /**
     * Checks if the book has been read.
     *
     * @return true if the book has been read, false otherwise
     */
    public boolean isRead() {
        return read;
    }

    /**
     * Marks the book as read or unread.
     *
     * @param read true to mark the book as read, false to mark it as unread
     */
    public void setRead(boolean read) {
        this.read = read;
    }

    /**
     * Sets a new rating for the book.
     *
     * @param rating the new rating for the book (1-5)
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * Provides a string representation of the book including the title, author,
     * rating, and whether it has been read.
     *
     * @return a string representation of the book
     */
    @Override
    public String toString() {
        return title + " by " + author + ", Rating: " + rating + (read ? " (Read)" : " (Unread)");
    }
}