# Testing Guide

This document explains how to run the existing JUnit 4 test suite, what each
test class covers, and how to add new tests when extending the application.

---

## Quick Start

### Prerequisites

- Java 11+
- JUnit 4 jar on the classpath (download
  [junit-4.13.2.jar](https://search.maven.org/artifact/junit/junit/4.13.2/jar)
  and
  [hamcrest-core-1.3.jar](https://search.maven.org/artifact/org.hamcrest/hamcrest-core/1.3/jar)
  if not already present)

### Compile

```bash
# From the repo root
javac -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar *.java
```

### Run all tests

```bash
java -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar   org.junit.runner.JUnitCore BookTest LibraryTest
```

Expected output on a clean pass:

```
JUnit version 4.13.2
..........
Time: 0.042

OK (10 tests)
```

### Run a single test class

```bash
java -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar   org.junit.runner.JUnitCore BookTest
```

---

## Test Classes

### BookTest.java

Unit tests for the `Book` model class.

| Test method | What it verifies |
|-------------|-----------------|
| `testConstructorAndGetters` | All fields set correctly via constructor |
| `testSetRating` | `setRating()` updates the rating field |
| `testSetStatus` | `setStatus()` toggles read/unread state |
| `testToString` | `toString()` produces a parseable, pipe-delimited line |
| `testFromString` | `Book.fromString()` round-trips a serialised line back to a Book |

These tests are pure unit tests — no file I/O, no GUI, no external state.

### LibraryTest.java

Integration tests for the `Library` model class (the in-memory data store).

| Test method | What it verifies |
|-------------|-----------------|
| `testAddBook` | `addBook()` increases the book count |
| `testSearchByTitle` | Title search returns the correct book |
| `testSearchByAuthor` | Author search is case-insensitive |
| `testSearchByGenre` | Genre filter returns only matching books |
| `testRemoveBook` | `removeBook()` reduces count and book is gone |
| `testToReadList` | Books marked to-read appear in the to-read list |

These tests construct a fresh `Library` instance in `@Before` so each test is
fully isolated.

---

## Writing New Tests

### Conventions

1. Create a new `*Test.java` file in the project root (same level as the class
   under test).
2. Annotate each test method with `@Test`.
3. Use `@Before` to set up fresh state before each test method.
4. Use `@After` to clean up temporary files if your test touches the filesystem.
5. Use `assertEquals`, `assertTrue`, `assertFalse`, `assertNull`, and
   `assertNotNull` from `org.junit.Assert`.

### Example: testing a new `Genre` feature

```java
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GenreTest {

    private Library lib;

    @Before
    public void setUp() {
        lib = new Library();
        lib.addBook(new Book("Dune", "Herbert", "Sci-Fi", 1965));
        lib.addBook(new Book("Foundation", "Asimov", "Sci-Fi", 1951));
        lib.addBook(new Book("1984", "Orwell", "Dystopia", 1949));
    }

    @Test
    public void testGetAllGenres() {
        // When you add a getGenres() method to Library
        java.util.Set<String> genres = lib.getGenres();
        assertTrue(genres.contains("Sci-Fi"));
        assertTrue(genres.contains("Dystopia"));
        assertEquals(2, genres.size());
    }

    @Test
    public void testFilterByGenreIsCaseInsensitive() {
        java.util.List<Book> results = lib.searchByGenre("sci-fi");
        assertEquals(2, results.size());
    }
}
```

### Testing GUI components

GUI tests require a running AWT/Swing event dispatch thread.  For headless
environments (CI, servers without a display) set the `java.awt.headless`
system property:

```bash
java -Djava.awt.headless=true   -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar   org.junit.runner.JUnitCore BookTest LibraryTest
```

Avoid testing Swing components directly; instead, extract business logic into
the model layer (`Book`, `Library`) and keep the GUI as thin as possible.

---

## CI Integration

Tests run automatically on every push via GitHub Actions (see
`.github/workflows/ci.yml`).  The workflow:

1. Sets up Java 11
2. Downloads JUnit 4 jars
3. Compiles all `.java` files
4. Runs `JUnitCore BookTest LibraryTest`
5. Fails the build if any test fails

To add a new test class to CI, append its name to the `JUnitCore` invocation
in the workflow file.
