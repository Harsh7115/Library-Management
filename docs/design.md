# Design Document — Library Management App

## Overview

A Java desktop application for managing a personal book library. Built with **Swing** for the GUI, following a strict **MVC (Model-View-Controller)** architecture, with **JUnit 5** for unit testing.

---

## Architecture

```
src/
├── model/          # Plain Java objects + business logic
│   ├── Book.java
│   ├── BookStatus.java
│   └── Library.java
├── view/           # Swing panels and frames
│   ├── MainFrame.java
│   ├── BookListPanel.java
│   ├── AddBookPanel.java
│   ├── SearchPanel.java
│   └── RatingPanel.java
├── controller/     # Wires model and view, handles events
│   └── LibraryController.java
├── persistence/    # JSON serialisation / deserialisation
│   └── BookRepository.java
└── Main.java       # Entry point
```

---

## Model Layer

### Book.java

| Field | Type | Notes |
|-------|------|-------|
| id | UUID | Auto-generated on construction |
| title | String | Non-empty |
| author | String | Non-empty |
| isbn | String | Optional; validated against 13-digit pattern |
| status | BookStatus | WANT_TO_READ, READING, READ |
| rating | int | 0-5 (0 = unrated) |
| notes | String | Free-form user notes |
| addedAt | LocalDate | Set on creation |

### Library.java

Maintains the canonical list of Book objects in memory. Key methods:

```java
void add(Book book);
boolean remove(UUID id);
Optional<Book> findById(UUID id);
List<Book> search(String query);   // searches title + author (case-insensitive)
List<Book> filterByStatus(BookStatus status);
List<Book> sortedByRating();
List<Book> sortedByTitle();
```

All mutations fire a PropertyChangeEvent so views can observe and refresh without polling.

---

## View Layer

Views are pure Swing components — they contain **no business logic** and hold no direct reference to Library. All interactions are delegated to the controller via action listeners.

| Panel | Responsibility |
|-------|---------------|
| MainFrame | Top-level JFrame; hosts card layout switching between panels |
| BookListPanel | Scrollable table (JTable) of all books; double-click to open detail |
| AddBookPanel | Form to add or edit a book; validates required fields before enabling Save |
| SearchPanel | Live search bar that filters the list as the user types |
| RatingPanel | 5-star rating widget (custom JComponent using filled/empty star icons) |

---

## Controller Layer

LibraryController is the single controller. It:

1. Holds references to both the model (Library) and all view panels.
2. Registers action listeners on view buttons.
3. Translates UI events into model calls.
4. Subscribes to model PropertyChangeEvents and tells affected views to repaint.

This keeps the model and views completely decoupled — neither knows the other exists.

---

## Persistence

BookRepository serialises the Library to a JSON file in the user home directory (~/.library-app/books.json) using Gson. The file is read on startup and written on every mutation.

Format example:
```json
[
  {
    "id": "3f8a1b2c-...",
    "title": "The Pragmatic Programmer",
    "author": "Hunt and Thomas",
    "isbn": "9780135957059",
    "status": "READ",
    "rating": 5,
    "notes": "Essential reading.",
    "addedAt": "2025-09-12"
  }
]
```

---

## Testing Strategy

All tests live in src/test/ and are run with JUnit 5 + Maven Surefire.

| Test class | What it covers |
|------------|---------------|
| BookTest | Constructor validation, getters/setters, equality |
| LibraryTest | add/remove/find, search correctness, sort order, event firing |
| BookRepositoryTest | Serialise to deserialise round-trip, corrupt-file handling |
| LibraryControllerTest | UI events trigger correct model mutations (mock views with Mockito) |

Run with:
```bash
mvn test
```

---

## Design Decisions and Trade-offs

**Why Swing over JavaFX?** The project targets JDK 11+ environments where Swing is always available; JavaFX requires a separate download on some distros.

**Why Gson over Jackson?** Gson has zero configuration for simple POJOs, keeping the persistence layer concise.

**Why PropertyChangeSupport over a custom observer?** It is part of the JDK, well-tested, and integrates naturally with Swing event dispatch thread.

**Planned improvements:**
- Multi-shelf support (Fiction, Non-fiction, etc.)
- CSV/Goodreads import
- Due-date tracking for borrowed books
- Search suggestions with trie-backed autocomplete
