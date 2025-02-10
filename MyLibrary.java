// Author: Ashish Dev Choudhary (ashishdev13) & Harshit Jain (hjain)
/**
  MyLibrary class provides the UI to interact with the Library system for user.
  It allows users to perform various operations such as searching, adding book,
  setting books as read, rating books, adding multiple books, suggesting books and 
  getting suggestions.
  
  Encapsulation is maintained by making the Library object private. We only expose
  relevant functionality through methods. This keeps the internal data structure 
  hidden from external manipulation. 
*/

import java.util.*; 

public class MyLibrary {

    private Library library; 
    private Scanner scanner;

    /**
      Constructor that initializes the library and scanner objects.
    */
    public MyLibrary() {
        library = new Library();  
        scanner = new Scanner(System.in);
    }


    /**
      Main method that starts the library.
      @param args Command-line arguments (not used).
    */
    public static void main(String[] args) {
        MyLibrary myLibrary = new MyLibrary();
        myLibrary.gameRun(); // Starts the Library
    }

    /**
      Method to run the main loop of the application, prompting for user commands.
      Basically the UI for program.
    */
    public void gameRun() {
        String command;
        do {
            System.out.println("\nEnter a command (search, addBook, setToRead, rate, getBooks, suggestRead, addBooks, quit):");
            command = scanner.nextLine();
            switch (command) {
                case "search":
                    search();  // Calls the search method to find books based on criteria.
                    break;
                case "addBook":
                    addBook();  // Calls the addBook method to add a new book to the library.
                    break;
                case "setToRead":
                    setToRead();  // Marks a book as read.
                    break;
                case "rate":
                    rateBook();  // Allows the user to rate a book.
                    break;
                case "getBooks":
                    getBooks();  // Retrieves books based on different filters.
                    break;
                case "suggestRead":
                    suggestRead();  // Suggests a random unread book to the user.
                    break;
                case "addBooks":
                    addBooks();  // Adds multiple books from a file.
                    break;
                case "quit":
                    System.out.println("Exiting the library system.");  // Exits the application.
                    break;
                default:
                    System.out.println("Invalid command.");
            }
        } while (!command.equals("quit"));  // Loop continues until 'quit' is entered.
    }

    /**
     Prompts the user to search the library by title, author, or rating.
    */
    public void search() {
        System.out.println("Search by (title, author, rating):");
        String option = scanner.nextLine();
        switch (option) {
            case "title":
                System.out.print("Enter title: ");
                String title = scanner.nextLine();
                List<Book> titleBooks = library.searchByTitle(title);  // Get a list of books with the given title
                for (Book book : titleBooks) {  
                    System.out.println(book);
                }
                break;

            case "author":
                System.out.print("Enter author: ");
                String author = scanner.nextLine();
                List<Book> authorBooks = library.searchByAuthor(author);  // Get a list of books by the given author
                for (Book book : authorBooks) {  
                    System.out.println(book);
                }
                break;

            case "rating":
                System.out.print("Enter rating (1-5): ");
                int rating = Integer.parseInt(scanner.nextLine());
                List<Book> ratedBooks = library.searchByRating(rating);  // Get a list of books with the given rating
                for (Book book : ratedBooks) {  
                    System.out.println(book);
                }
                break;

            default:
                System.out.println("Invalid search option.");
        }
    }


    /**
     Prompts the user to add a new book to the library by entering the title and author.
    */
    public void addBook() {
        System.out.print("Enter title: ");
        String title = scanner.nextLine();
        System.out.print("Enter author: ");
        String author = scanner.nextLine();
        int rating = 0;  // Default rating is set to 0 when a book is added.
        library.addBook(new Book(title, author, rating));
    }

    /**
     Prompts the user to mark a book as read by entering the title.
    */
    public void setToRead() {
        System.out.print("Enter the title of the book to mark as read: ");
        String title = scanner.nextLine();
        library.setToRead(title); 
    }

    /**
     Prompts the user to rate a book by entering the title and a new rating.
    */
    public void rateBook() {
        System.out.print("Enter the title of the book to rate: ");
        String title = scanner.nextLine();
        System.out.print("Enter the new rating (1-5): ");
        int rating = Integer.parseInt(scanner.nextLine());
        library.rateBook(title, rating);
    }

    /**
      Displays the books in the library based on various filters (title, author, read, unread).
    */
    public void getBooks() {
        System.out.println("Options: (1) all books by title, (2) all books by author, (3) read books, (4) unread books");
        int option = Integer.parseInt(scanner.nextLine());
        switch (option) {
            case 1:
                // Get a list of all books sorted by title
                List<Book> booksByTitle = library.getAllBooksSortedByTitle();
                for (Book book : booksByTitle) {  
                    System.out.println(book);  
                }
                break;

            case 2:
                // Get a list of all books sorted by author
                List<Book> booksByAuthor = library.getAllBooksSortedByAuthor();
                for (Book book : booksByAuthor) {  
                    System.out.println(book);  
                }
                break;

            case 3:
                // Get a list of all read books
                List<Book> readBooks = library.getReadBooks();
                for (Book book : readBooks) {  
                    System.out.println(book);  
                }
                break;

            case 4:
                // Get a list of all unread books
                List<Book> unreadBooks = library.getUnreadBooks();
                for (Book book : unreadBooks) {  
                    System.out.println(book);  
                }
                break;

            default:
                System.out.println("Invalid option.");
        }
    }


    /**
     Suggests a random unread book for the user to read.
    */
    public void suggestRead() {
        Book book = library.suggestRandomUnreadBook();  // Suggests an unread book.
        if (book != null) {
            System.out.println("We suggest you read: " + book);  
        } else {
            System.out.println("No unread books to suggest.");  
        }
    }

    /**
     Prompts the user to add multiple books from a file by entering the file name.
    */
    public void addBooks() {
        System.out.print("Enter the file name: ");
        String filename = scanner.nextLine();
        library.addBooksFromFile(filename);  
    }
}
