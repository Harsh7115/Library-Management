import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.List;

/**
 * LibraryGUI class provides a graphical user interface for managing a library system.
 * Users can add books, load books from a file, search for books, mark books as read, 
 * rate books, list books, and receive book suggestions.
 * 
 * Author: Ashish Dev Choudhary (ashishdev13) & Harshit Jain (hjain)
 */

public class LibraryGUI { 
    private Library library;

    /**
     * Constructor initializes the LibraryGUI with a given Library instance
     * and creates the GUI.
     *
     * @param library the Library object to manage book data
     */
    public LibraryGUI(Library library) { 
        this.library = library;
        createAndShowGUI();
    }
    
    
    /**
     * Creates and displays the main GUI frame and its panels.
     */
    private void createAndShowGUI() {
        JFrame frame = new JFrame("Library Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);

        // Create panels for each functionality
        JPanel addBookPanel = createAddBookPanel();
        JPanel addBooksFromFilePanel = createAddBooksFromFilePanel(); 
        JPanel searchPanel = createSearchPanel();
        JPanel markReadPanel = createMarkReadPanel();
        JPanel ratePanel = createRatePanel();
        JPanel listPanel = createListPanel();
        JPanel suggestPanel = createSuggestPanel();

        // Add panels to a tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Add Book", addBookPanel);
        tabbedPane.addTab("Add Books from File", addBooksFromFilePanel); 
        tabbedPane.addTab("Search Books", searchPanel);
        tabbedPane.addTab("Mark as Read", markReadPanel);
        tabbedPane.addTab("Rate Book", ratePanel);
        tabbedPane.addTab("List Books", listPanel);
        tabbedPane.addTab("Suggest Book", suggestPanel);

        frame.add(tabbedPane);
        frame.setVisible(true);
    }

    /**
     * Creates a panel for adding a single book.
     *
     * @return JPanel with fields and a button for adding books
     */
    private JPanel createAddBookPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2));
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JButton addBookButton = new JButton("Add Book");
        
        // Add components to the panel
        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Author:"));
        panel.add(authorField);
        panel.add(addBookButton);

         // Action listener for adding a book
        addBookButton.addActionListener(e -> {
            String title = titleField.getText();
            String author = authorField.getText();
            if (!title.isEmpty() && !author.isEmpty()) {
                library.addBook(new Book(title, author, 0));
                JOptionPane.showMessageDialog(null, "Book added successfully.");
                titleField.setText("");
                authorField.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "Please fill in all fields.");
            }
        });

        return panel;
    }

    /**
     * Creates a panel for loading books from a file.
     *
     * @return JPanel with a button for loading books from a file
     */
    private JPanel createAddBooksFromFilePanel() {
        JPanel panel = new JPanel();
        JButton loadBooksButton = new JButton("Load Books from File");
        
        panel.add(loadBooksButton);
        
        // Action listener for loading books from a file
        loadBooksButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String fileName = selectedFile.getAbsolutePath(); 
                library.addBooksFromFile(fileName);
            }
        });
        return panel;
    }

    /**
     * Creates a panel for searching books by title, author, or rating.
     *
     * @return JPanel with search options and results display
     */
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JComboBox<String> searchTypeComboBox = new JComboBox<>(new String[]{"Title", "Author", "Rating"});
        JTextField searchField = new JTextField();
        JButton searchButton = new JButton("Search");
        JTextArea resultsArea = new JTextArea(10, 30);
        resultsArea.setEditable(false);

        // Add components to the panel
        panel.add(searchTypeComboBox, BorderLayout.NORTH);
        panel.add(searchField, BorderLayout.CENTER);
        panel.add(searchButton, BorderLayout.SOUTH);
        panel.add(new JScrollPane(resultsArea), BorderLayout.EAST);

        // Action listener for searching books
        searchButton.addActionListener(e -> {
            String searchType = (String) searchTypeComboBox.getSelectedItem();
            String searchQuery = searchField.getText();
            if (!searchQuery.isEmpty()) {
                List<Book> results;
                switch (searchType) {
                    case "Title":
                        results = library.searchByTitle(searchQuery);
                        break;
                    case "Author":
                        results = library.searchByAuthor(searchQuery);
                        break;
                    case "Rating":
                        try {
                            int rating = Integer.parseInt(searchQuery);
                            results = library.searchByRating(rating);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Please enter a valid number for rating.");
                            return;
                        }
                        break;
                    default:
                        results = List.of();
                }
                resultsArea.setText(formatBookList(results));
            } else {
                JOptionPane.showMessageDialog(null, "Please enter a search query.");
            }
        });

        return panel;
    }

    /**
     * Creates a panel for marking a book as read.
     *
     * @return JPanel with input field and button for marking books as read
     */
    private JPanel createMarkReadPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        JTextField titleField = new JTextField();
        JButton markReadButton = new JButton("Mark as Read");
        

        // Add components to the panel
        panel.add(new JLabel("Title of the book to mark as read:"));
        panel.add(titleField);
        panel.add(markReadButton);

        // Action listener for marking a book as read
        markReadButton.addActionListener(e -> {
            String title = titleField.getText();
            if (!title.isEmpty()) {
                boolean success = library.setToRead(title);
                if (success) {
                    JOptionPane.showMessageDialog(null, "Book marked as read.");
                } else {
                    JOptionPane.showMessageDialog(null, "Book not found.");
                }
                titleField.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "Please enter a book title.");
            }
        });

        return panel;
    }

    /**
     * Creates a panel for rating a book.
     *
     * @return JPanel with input fields and a button for rating books
     */
    private JPanel createRatePanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2));
        JTextField titleField = new JTextField();
        JTextField ratingField = new JTextField();
        JButton rateButton = new JButton("Rate Book");
        

        // Add components to the panel
        panel.add(new JLabel("Title of the book:"));
        panel.add(titleField);
        panel.add(new JLabel("Rating (1-5):"));
        panel.add(ratingField);
        panel.add(rateButton);

        // Action listener for rating a book
        rateButton.addActionListener(e -> {
            String title = titleField.getText();
            try {
                int rating = Integer.parseInt(ratingField.getText());
                if (rating >= 1 && rating <= 5) {
                    boolean success = library.rateBook(title, rating);
                    if (success) {
                        JOptionPane.showMessageDialog(null, "Book rated successfully.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Book not found.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a rating between 1 and 5.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid rating input.");
            }
            titleField.setText("");
            ratingField.setText("");
        });

        return panel;
    }

    /**
     * Creates a panel for listing books sorted by title or author.
     *
     * @return JPanel with buttons for listing books and a display area
     */
    private JPanel createListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JButton listByTitleButton = new JButton("List by Title");
        JButton listByAuthorButton = new JButton("List by Author");
        JTextArea listArea = new JTextArea(15, 50);
        listArea.setEditable(false);

        // Add components to the panel
        panel.add(listByTitleButton, BorderLayout.NORTH);
        panel.add(listByAuthorButton, BorderLayout.CENTER);
        panel.add(new JScrollPane(listArea), BorderLayout.SOUTH);

        // Action listeners for listing books
        listByTitleButton.addActionListener(e -> {
            List<Book> books = library.getAllBooksSortedByTitle();
            listArea.setText(formatBookList(books));
        });

        listByAuthorButton.addActionListener(e -> {
            List<Book> books = library.getAllBooksSortedByAuthor();
            listArea.setText(formatBookList(books));
        });

        return panel;
    }

    /**
     * Creates a panel for suggesting a book to read next.
     *
     * @return JPanel with a suggestion button and display area
     */
    private JPanel createSuggestPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        JButton suggestButton = new JButton("Suggest Unread Book");
        JLabel suggestLabel = new JLabel("Suggestion:");

        panel.add(suggestButton);
        panel.add(suggestLabel);

        // Action listener for suggesting a book
        suggestButton.addActionListener(e -> {
            Book suggestion = library.suggestRandomUnreadBook();
            if (suggestion != null) {
                suggestLabel.setText("Suggested Book: " + suggestion.getTitle() + " by " + suggestion.getAuthor());
            } else {
                suggestLabel.setText("No unread books available.");
            }
        });

        return panel;
    }

    /**
     * Formats a list of books into a readable string.
     *
     * @param books the list of books to format
     * @return a formatted string of book details
     */
    private String formatBookList(List<Book> books) {
        String result = "";
        for (Book book : books) {
            result += book.getTitle() + " by " + book.getAuthor() + " | Rating: " + book.getRating() + "\n";
        }
        return result;
    }


    public static void main(String[] args) {
        Library library = new Library(); 
        new LibraryGUI(library);
    }
}
