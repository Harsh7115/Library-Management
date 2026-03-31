# Persistence Reference — Library Management System

This document describes how Library Management System stores and loads book
data, the exact on-disk format, and how to safely migrate or back up your
library.

---

## Storage file: `books.txt`

All book data is written to a single plain-text file called **`books.txt`**
located in the working directory (the folder from which you launched the app).

The file is **human-readable and human-editable** — you can open it in any
text editor, but be careful to follow the format rules below or the app will
skip malformed lines on the next load.

---

## Record format

Each line in `books.txt` represents exactly one book:

```
<title>|<author>|<genre>|<rating>|<toRead>
```

| Column | Type | Constraints |
|---|---|---|
| `title` | String | Non-empty; must not contain `|` |
| `author` | String | Non-empty; must not contain `|` |
| `genre` | String | Non-empty; must not contain `|` |
| `rating` | int | 0–5 (0 = not yet rated) |
| `toRead` | boolean | `true` or `false` (lowercase) |

**Example `books.txt`:**

```
Dune|Frank Herbert|Sci-Fi|5|false
The Pragmatic Programmer|David Thomas|Tech|4|true
Project Hail Mary|Andy Weir|Sci-Fi|5|false
Atomic Habits|James Clear|Self-Help|0|true
Clean Code|Robert C. Martin|Tech|4|false
```

---

## Read path (`Library.loadFromFile`)

On startup, `Library.loadFromFile()` performs the following steps:

1. Open `books.txt` for reading (creates an empty file if it does not exist).
2. Read line by line using `BufferedReader`.
3. For each line:
   - Skip blank lines.
   - Split on `|` — if fewer than 5 tokens result, log a warning to
     `System.err` and skip the line.
   - Parse `rating` with `Integer.parseInt`; skip the line on
     `NumberFormatException`.
   - Parse `toRead` with `Boolean.parseBoolean` (any value other than
     `"true"` is treated as `false`).
   - Construct a `Book` object and append it to the internal list.
4. Close the reader.

**Error handling policy** — corrupt lines are skipped silently (with a
`System.err` message) rather than crashing the app. This means a single
bad line will not prevent the rest of the library from loading.

---

## Write path (`Library.saveToFile`)

Every mutating operation (add, remove, update rating, toggle to-read) calls
`saveToFile()` immediately after modifying the in-memory list.

The write strategy is a **full rewrite**:

1. Open `books.txt` for writing (truncates the file).
2. Iterate over the entire in-memory book list.
3. Write each book as a single pipe-delimited line followed by `\n`.
4. Flush and close the writer.

**Why full rewrite?**  
For a personal library (expected size: a few hundred books), rewriting the
whole file on every mutation is fast (< 1 ms) and simple to reason about.
There is no risk of leaving a partial record in the middle of the file from
an interrupted write — the OS buffers the new content and commits it
atomically at close time on most filesystems.

A future optimisation (for libraries with thousands of entries) would be an
append-only log with periodic compaction, but this is not needed today.

---

## Backup and migration

### Making a backup

Copy `books.txt` to a safe location:

```bash
cp books.txt books-backup-$(date +%Y%m%d).txt
```

### Restoring a backup

Replace the current `books.txt` with your backup and restart the app.

### Migrating from a spreadsheet

Export your book list as CSV, then convert it to the pipe-delimited format:

```python
import csv, pathlib

reader = csv.DictReader(open("books.csv"))
with open("books.txt", "w") as out:
    for row in reader:
        title  = row["title"].replace("|", "-")
        author = row["author"].replace("|", "-")
        genre  = row.get("genre", "Unknown").replace("|", "-")
        rating = int(row.get("rating", 0))
        to_read = str(row.get("toRead", "false")).lower() == "true"
        out.write(f"{title}|{author}|{genre}|{rating}|{to_read}\n")
```

### Adding books from the command line

You can append a book directly without launching the GUI:

```bash
echo "The Mythical Man-Month|Frederick Brooks|Tech|5|false" >> books.txt
```

The app will pick it up on the next launch (or on next save, whichever comes
first — save always overwrites, so add books via CLI only when the app is
closed).

---

## Known limitations

| Limitation | Impact | Workaround |
|---|---|---|
| Pipe character `|` not allowed in fields | A title/author/genre containing `|` will corrupt the record | The GUI strips `|` on input; for manual edits, replace with a dash |
| No concurrent access support | Two instances of the app running simultaneously will overwrite each other's saves | Run only one instance at a time |
| No automatic backup on write | A crash mid-write could truncate the file | Keep a periodic backup via cron or a cloud sync folder |
| Full rewrite on every mutation | For very large libraries (10 000+ books) this may introduce noticeable lag | Not a concern for typical personal use |
