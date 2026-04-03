# Extending the Library Management System

This guide explains how to add new features to the Library Management System
without breaking existing functionality.  It follows the MVC pattern already
established in the codebase (see `docs/design.md` for the architecture
overview).

---

## Table of Contents

1. [Adding a new field to Book](#1-adding-a-new-field-to-book)
2. [Adding a new search filter](#2-adding-a-new-search-filter)
3. [Adding a new sort order](#3-adding-a-new-sort-order)
4. [Adding a new GUI panel or dialog](#4-adding-a-new-gui-panel-or-dialog)
5. [Writing tests for new features](#5-writing-tests-for-new-features)
6. [Updating persistence when the model changes](#6-updating-persistence-when-the-model-changes)

---

## 1. Adding a new field to Book

**Example:** add a `pageCount` integer field.

### Step 1 — Update `Book.java`

```java
public class Book {
    // existing fields ...
    private int pageCount;   // NEW

    // Update the constructor
    public Book(String title, String author, String genre,
                int rating, boolean toRead, int pageCount) {
        // existing assignments ...
        this.pageCount = pageCount;  // NEW
    }

    // Add getter and setter
    public int getPageCount()           { return pageCount; }
    public void setPageCount(int pages) { this.pageCount = pages; }

    @Override
    public String toString() {
        // Add pageCount to the serialised representation so
        // Library.java can persist it (see Section 6).
        return title + "," + author + "," + genre + ","
             + rating + "," + toRead + "," + pageCount;
    }
}
```

### Step 2 — Update `Library.java` (parsing)

In the `loadBooks()` method, increment the expected token count and read the
new field:

```java
// Before (5 tokens):
// String[] parts = line.split(",");
// books.add(new Book(parts[0], parts[1], parts[2],
//                    Integer.parseInt(parts[3]),
//                    Boolean.parseBoolean(parts[4])));

// After (6 tokens):
String[] parts = line.split(",");
int pages = parts.length > 5 ? Integer.parseInt(parts[5].trim()) : 0;
books.add(new Book(parts[0], parts[1], parts[2],
                   Integer.parseInt(parts[3]),
                   Boolean.parseBoolean(parts[4]),
                   pages));
```

> **Backwards compatibility:** The `parts.length > 5` guard ensures old
> `books.txt` files (without the new column) still load correctly with a
> default of 0 pages.

### Step 3 — Update `LibraryGUI.java`

Add an input field in the "Add Book" dialog and pass the value to the
`Book` constructor when the user clicks **Save**.

---

## 2. Adding a new search filter

**Example:** filter by minimum rating.

### Step 1 — Add a method to `Library.java`

```java
/**
 * Returns all books with a rating >= minRating.
 *
 * @param minRating inclusive lower bound (1–5)
 * @return unmodifiable list of matching books
 */
public List<Book> searchByMinRating(int minRating) {
    List<Book> result = new ArrayList<>();
    for (Book b : books) {
        if (b.getRating() >= minRating) {
            result.add(b);
        }
    }
    return Collections.unmodifiableList(result);
}
```

### Step 2 — Wire it up in `LibraryGUI.java`

Add a **Min Rating** spinner to the search panel and call
`library.searchByMinRating(spinner.getValue())` when the user submits a
search.

### Step 3 — Add a unit test

```java
// In LibraryTest.java
@Test
public void testSearchByMinRating_returnsOnlyHighRatedBooks() {
    library.addBook(new Book("Dune", "Herbert", "Sci-Fi", 5, false, 412));
    library.addBook(new Book("Flatland", "Abbott", "Math", 2, false, 96));

    List<Book> result = library.searchByMinRating(4);

    assertEquals(1, result.size());
    assertEquals("Dune", result.get(0).getTitle());
}
```

---

## 3. Adding a new sort order

**Example:** sort by page count (ascending).

### Step 1 — Add a `Comparator` to `Library.java`

```java
public static final Comparator<Book> BY_PAGE_COUNT =
    Comparator.comparingInt(Book::getPageCount);

/**
 * Returns all books sorted by the given comparator.
 */
public List<Book> getSorted(Comparator<Book> comparator) {
    List<Book> sorted = new ArrayList<>(books);
    sorted.sort(comparator);
    return Collections.unmodifiableList(sorted);
}
```

### Step 2 — Expose it in the GUI

Add a **Sort by** drop-down in the toolbar.  On change, call
`library.getSorted(Library.BY_PAGE_COUNT)` and refresh the book list model.

---

## 4. Adding a new GUI panel or dialog

The GUI is structured as a single `JFrame` (in `LibraryGUI.java`) with
tabbed or card-layout panels.  To add a new panel:

1. Create a helper method `private JPanel buildMyPanel()` that constructs and
   returns the new panel.
2. Register it with the layout manager or tab pane inside the existing
   `initComponents()` method.
3. Attach action listeners that call the appropriate `Library` methods.
4. Call `refreshBookList()` (or equivalent) after any mutation so the model
   and view stay in sync.

```java
// Skeleton for a "Statistics" panel
private JPanel buildStatisticsPanel() {
    JPanel panel = new JPanel(new GridLayout(0, 2, 8, 4));

    panel.add(new JLabel("Total books:"));
    panel.add(new JLabel(String.valueOf(library.getBooks().size())));

    double avg = library.getBooks().stream()
                        .mapToInt(Book::getRating)
                        .average()
                        .orElse(0.0);
    panel.add(new JLabel("Average rating:"));
    panel.add(new JLabel(String.format("%.1f", avg)));

    return panel;
}
```

---

## 5. Writing tests for new features

All tests live alongside the source files (`BookTest.java`,
`LibraryTest.java`).  Follow these conventions:

- **One assertion per test** where possible — keeps failures informative.
- **Arrange / Act / Assert** structure.
- **Test edge cases**: empty library, duplicate entries, null inputs.

```java
@Test
public void testAddBook_incrementsSize() {
    int before = library.getBooks().size();
    library.addBook(new Book("1984", "Orwell", "Dystopia", 5, false, 328));
    assertEquals(before + 1, library.getBooks().size());
}

@Test
public void testGetBooks_returnsUnmodifiableList() {
    List<Book> books = library.getBooks();
    assertThrows(UnsupportedOperationException.class,
                 () -> books.add(new Book("Test", "X", "Y", 1, false, 0)));
}
```

Run the full suite from the project root:

```bash
javac -cp .:junit-4.13.jar:hamcrest-core-1.3.jar *.java
java  -cp .:junit-4.13.jar:hamcrest-core-1.3.jar       org.junit.runner.JUnitCore BookTest LibraryTest
```

---

## 6. Updating persistence when the model changes

`Library.java` reads and writes `books.txt` as a CSV-like flat file.  When
you add fields to `Book`, make sure both the serialisation (`toString()`) and
the deserialisation (`loadBooks()`) are updated together.

**Checklist for a model change:**

- [ ] `Book.toString()` emits the new field in the correct column position.
- [ ] `Library.loadBooks()` reads the new column with a backwards-compatible
      default for files written by older versions.
- [ ] Existing `books.txt` in the repo is updated (or left as-is if the
      default value is acceptable).
- [ ] `BookTest` and `LibraryTest` cover the new round-trip (save → reload →
      assert fields match).
