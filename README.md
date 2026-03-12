# Library Management System

![Java](https://img.shields.io/badge/Java-ED8B00?style=flat&logo=java&logoColor=white)
![Swing](https://img.shields.io/badge/Swing-GUI-blue?style=flat)
![JUnit](https://img.shields.io/badge/Tested%20with-JUnit-green?style=flat)
![OOP](https://img.shields.io/badge/Design-OOP%20%7C%20MVC-purple?style=flat)

A Java desktop application for managing a personal book library. Built with Swing for the GUI, follows object-oriented design principles, and ships with a full JUnit test suite.

## Features

- **Add Books** — store title, author, genre, and metadata
- **Search** — find books by title, author, or genre
- **To-Read List** — mark books for future reading
- **Ratings** — rate books after reading
- **Smart Suggestions** — get reading recommendations based on your preferences
- **Persistent Storage** — library state saved to `books.txt`
- **Automated Tests** — JUnit tests covering core library logic

## Project Structure

```
Library-Management/
├── Book.java              # Book model (title, author, genre, rating)
├── Library.java           # Core library logic and data store
├── LibraryGUI.java        # Swing GUI — main window and controls
├── MyLibrary.java         # Application entry point
├── BookTest.java          # JUnit tests for Book
├── LibraryTest.java       # JUnit tests for Library
└── books.txt              # Persistent book data
```

## Getting Started

**Requirements:** Java 11+, JUnit 4 (for tests)

```bash
# Compile
javac *.java

# Run
java MyLibrary

# Run tests
java -cp .:junit-4.13.jar org.junit.runner.JUnitCore BookTest LibraryTest
```

## Design

- **MVC pattern** — `Library.java` is the model, `LibraryGUI.java` is the view/controller
- **Object-oriented** — `Book` encapsulates all book attributes and behavior
- **File persistence** — custom serialization to flat file for simplicity
- **Extensible** — adding new book attributes or search criteria is straightforward

## Tech Stack

| Area | Technology |
|------|-----------|
| Language | Java 11 |
| GUI | Java Swing |
| Testing | JUnit 4 |
| Build | javac / manual |

<!-- Last reviewed: March 2026 -->
