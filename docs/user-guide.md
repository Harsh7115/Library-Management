# User Guide

This guide walks an end user through the main flows in the Library Management app.
It complements the developer-facing `architecture.md` and `design.md`.

## Launching the app

From the project root:

```
javac *.java
java LibraryApp
```

The main window opens with four tabs: **Catalog**, **To-Read**, **Ratings**, and
**Account**. The status bar at the bottom shows how many books are currently
loaded and the path of the persistence file in use.

## Catalog tab

The Catalog tab is where books are added, edited, and searched.

### Adding a book

1. Click **Add Book**.
2. Enter title, author, ISBN, and (optionally) a publication year and genre.
3. Click **Save**. The new book is appended to the catalog and the file is flushed
   to disk immediately.

Validation rules:

- Title and author are required.
- ISBN is optional but, when provided, must be exactly 10 or 13 digits.
- Publication year, when provided, must be between 1450 and the current year.

### Searching

Type into the **Search** field at the top of the tab. The catalog is filtered
incrementally as you type. The search is case-insensitive and matches against title,
author, ISBN, and genre simultaneously.

Use the **Genre** dropdown to restrict the search to a single genre, or leave it on
**All** to search across the whole catalog.

### Editing or removing a book

Right-click any row to open the context menu:

- **Edit** opens the same dialog as **Add Book** with the row pre-populated.
- **Remove** asks for confirmation before deleting.
- **Add to To-Read** queues the book on the To-Read list.

## To-Read tab

The To-Read tab is a personal queue of books you intend to read. It is independent
of the catalog: removing a book from the catalog does not remove it from your
To-Read list, and a To-Read entry can reference a book that is no longer in the
catalog.

Drag-and-drop within the list to reorder entries. Mark a book as **Done** when
finished -- it disappears from the active list and shows up under **History** in
the same tab.

## Ratings tab

Each book in the catalog can have one rating from 1 to 5 stars and a free-form
review of up to 1000 characters. Ratings are stored alongside the book and are
included when you export the catalog.

The summary panel at the top of the Ratings tab shows the average rating and the
count of rated books. Sort by **Rating** to see your favourites at the top.

## Account tab

Account holds your display name and an optional email address. Neither is required
for the app to function -- they are only used to label exports.

Use **Export Catalog** to write the entire catalog (with ratings and reviews) to a
JSON file you choose, and **Import Catalog** to merge a previously exported file
back in. Imports are additive and never delete data.

## Persistence

All data is saved to a single JSON file under the user's home directory:

```
~/.library-management/library.json
```

Every successful change in the UI triggers a write. The file is fsynced before the
UI returns control, so a crash immediately after pressing **Save** still leaves the
data on disk.

## Keyboard shortcuts

| Shortcut       | Action                            |
|----------------|-----------------------------------|
| Ctrl+N         | Add new book                      |
| Ctrl+F         | Focus the search field            |
| Ctrl+S         | Manual save (normally automatic)  |
| Ctrl+E         | Export catalog                    |
| Ctrl+I         | Import catalog                    |
| Delete         | Remove the selected row           |
| F2             | Edit the selected row             |

## Getting help

Open **Help -> About** for the app version and a link to the GitHub repository.
Bug reports and feature requests belong on the issue tracker.
