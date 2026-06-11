# Library Management System

[![Java](https://img.shields.io/badge/Java-11-ED8B00?style=flat&logo=openjdk&logoColor=white)](https://openjdk.org)
[![Swing](https://img.shields.io/badge/GUI-Java%20Swing-blue?style=flat)](https://docs.oracle.com/javase/tutorial/uiswing/)
[![JUnit](https://img.shields.io/badge/Tests-JUnit%204-green?style=flat)](https://junit.org/junit4/)
[![License: MIT](https://img.shields.io/badge/License-MIT-lightgrey?style=flat)](LICENSE)

Java desktop app for managing a personal book library — add books, search by any field, build a to-read list, rate what you've read, and get recommendations. Built with Swing and a full JUnit test suite.

## Features

- **Add / remove books** — title, author, genre, and read status
- **Search** — filter by title, author, or genre in real time
- **To-read list** — queue books for later
- **Ratings** — 1–5 star ratings on completed reads
- **Recommendations** — genre-based suggestions from your existing library
- **Persistence** — library state saved to and loaded from `books.txt`

## Getting Started

**Requirements:** Java 11+

```bash
git clone https://github.com/Harsh7115/Library-Management
cd Library-Management

# Compile
javac *.java

# Run
java MyLibrary
```

### Run Tests

```bash
# Requires junit-4.13.jar and hamcrest-core-1.3.jar on classpath
javac -cp .:junit-4.13.jar:hamcrest-core-1.3.jar *.java
java  -cp .:junit-4.13.jar:hamcrest-core-1.3.jar org.junit.runner.JUnitCore BookTest LibraryTest
```

## Architecture

```
MyLibrary            entry point — creates Library and LibraryGUI
    │
    ├── Library      model: holds the book list, implements add/remove/search/recommend
    │     └── Book   data: title, author, genre, rating, readStatus
    │
    └── LibraryGUI   view + controller: Swing window, handles all user events
```

Follows **MVC**: `Library` owns all state and business logic, `LibraryGUI` owns layout and event dispatch. `LibraryGUI` never modifies `Book` objects directly — it calls `Library` methods, then re-reads state to refresh the display.

## Source Files

| File | Role |
|---|---|
| `Book.java` | Book model — fields, getters, `toString` for file serialization |
| `Library.java` | Core logic — add, remove, search, rate, recommend, load/save |
| `LibraryGUI.java` | Swing UI — panels, buttons, tables, event listeners |
| `MyLibrary.java` | `main()` — wires Library → GUI and launches the window |
| `BookTest.java` | JUnit tests for Book construction and field validation |
| `LibraryTest.java` | JUnit tests for add, remove, search, rating, and persistence |
| `books.txt` | Flat-file storage (auto-created on first run) |

## Design Notes

- **File persistence** uses a simple line-delimited format — each `Book.toString()` writes one record, `Library.load()` parses it back on startup. No external DB needed.
- **Recommendations** scan the existing library for books in the same genre as your highest-rated reads and surface unread ones first.
- **Search** runs a case-insensitive substring match across title, author, and genre simultaneously.

## Tech Stack

Java 11 · Java Swing · JUnit 4
