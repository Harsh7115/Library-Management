# Roadmap — Library Management System

Planned features and improvements, loosely ordered by priority. Contributions welcome — see [extending.md](extending.md) for how to add new functionality.

---

## v1.1 — Near Term

### Core Features
- [ ] **Bulk import from CSV / Goodreads export**  
  Parse a Goodreads-compatible CSV and import all books in one step. Deduplicate by ISBN.

- [ ] **Tag / label system**  
  Allow multiple free-form tags per book (e.g. "favourites", "loaned-out", "signed-copy"). Tags are filterable in the search panel.

- [ ] **Loan tracker**  
  Track books loaned to friends: borrower name, loan date, expected return date. Overdue loans highlighted in red.

- [ ] **Keyboard shortcut improvements**  
  Ensure all actions are reachable without a mouse. Add a shortcuts cheat-sheet dialog (`F1`).

### UI / UX
- [ ] **Dark mode theme**  
  Add a `LookAndFeel` toggle in Settings → Appearance. Persist selection to disk.

- [ ] **Sortable columns in all tables**  
  Click any column header to sort ascending/descending. Multi-column sort with Shift+click.

- [ ] **Inline editing in the book table**  
  Double-click a cell to edit title, author, genre, or rating without opening a separate dialog.

---

## v1.2 — Medium Term

### Search & Discovery
- [ ] **Full-text trigram search**  
  Replace O(n) substring scan with an in-memory trigram index for fuzzy, typo-tolerant matching at large scale (10k+ books).

- [ ] **Google Books API integration**  
  Search by title/author and auto-fill ISBN, cover image, description, and genre from the API response.

- [ ] **Book cover thumbnails**  
  Display cover art (fetched from Open Library or Google Books) in the book detail panel.

### Data
- [ ] **Multiple library profiles**  
  Support several named libraries (e.g. "Work", "Home"). Switch between them from the toolbar.

- [ ] **Export to PDF reading list**  
  Generate a formatted PDF of the current library or to-read queue using iText or Apache PDFBox.

- [ ] **Undo/redo stack**  
  Command pattern for all mutations so any accidental deletion can be undone (`Ctrl+Z`).

---

## v1.3 — Long Term

### Sync & Collaboration
- [ ] **Cloud sync via Google Drive**  
  Save the library JSON to Google Drive so it is accessible from multiple machines.

- [ ] **Shared reading lists**  
  Export a shareable link (or QR code) for a curated list of books.

### Platform
- [ ] **macOS `Cmd` aliases**  
  Map `Cmd` to `Ctrl` shortcuts on macOS for a native feel.

- [ ] **CLI mode**  
  A headless mode for scripting: `library-manager list`, `library-manager add --isbn ...`, etc.

- [ ] **Plugin API**  
  A well-defined extension point so third parties can add importers, exporters, or new panels.

---

## Known Limitations (to fix)

| Issue | Workaround | Target |
|---|---|---|
| Duplicate ISBNs silently overwrite | Validate before add | v1.1 |
| No backup on corruption | Manual copy | v1.1 |
| Sort resets after every edit | Re-apply sort manually | v1.2 |
| No pagination for large libraries | Filter to narrow results | v1.2 |

---

## How to Contribute

1. Pick an item from this list (or open an issue with your own idea)
2. Read [extending.md](extending.md) for the codebase conventions
3. Fork, branch off `main`, and open a pull request

All PRs should include JUnit tests for new model logic and pass `mvn test` cleanly.
