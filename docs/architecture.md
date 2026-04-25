# Architecture

This document describes the high-level structure of the Library-Management
application: how packages are layered, what the core domain types are, and how the
Swing UI is kept thin.

## Goals

- Keep domain logic free of UI dependencies so it stays unit-testable.
- Use plain Java collections and `Optional` rather than introducing third-party
  containers for trivial work.
- Treat persistence (the on-disk catalogue files) as an implementation detail
  behind a repository interface.

## Layering

The codebase follows a classic three-layer structure with a strict dependency
direction. Higher layers may reference lower layers, never the reverse.

```
+---------------------------+
|        ui (Swing)         |     panels, dialogs, table models
+---------------------------+
              |
              v
+---------------------------+
|         service           |     use-cases: borrow, return, search
+---------------------------+
              |
              v
+---------------------------+
|         domain            |     Book, Member, Loan, Rating
+---------------------------+
              |
              v
+---------------------------+
|       persistence         |     BookRepository, FileCatalogue
+---------------------------+
```

The compiler enforces this with package-private constructors on the domain types
and an explicit re-export module list in `module-info.java`.

## Core domain types

| Type     | Responsibility                                              |
|----------|-------------------------------------------------------------|
| `Book`   | Immutable record of a title, author, ISBN, and copy count   |
| `Member` | A library patron with an ID, contact info, and borrow limit |
| `Loan`   | A borrowed-book record with timestamps and return state     |
| `Rating` | A 1–5 star review left by a member on a book                |

Domain types validate their own invariants in their constructors. A negative copy
count, an empty ISBN, or a rating outside the 1–5 range will throw
`IllegalArgumentException` rather than be silently coerced.

## Use cases (service layer)

The service layer exposes a small set of cohesive operations. Each one is a single
public method on a service class and corresponds to exactly one user-visible
action in the UI.

- `BorrowService.borrow(memberId, isbn)` — atomic; checks borrow limit and copy
  availability, records the loan, decrements the available count.
- `BorrowService.returnLoan(loanId)` — looks up the loan, marks it returned,
  increments the copy count.
- `SearchService.byTitle(query)` — case-insensitive substring match with stable
  ordering by relevance then title.
- `ToReadService.add(memberId, isbn)` / `remove(...)` — manages the per-member
  to-read list.
- `RatingService.rate(memberId, isbn, stars)` — records or replaces the member's
  rating on a book.

Services do not hold UI references. They take a repository in their constructor
and return immutable result objects, which makes them straightforward to unit
test under JUnit.

## UI layer

The Swing UI is kept deliberately thin. Each panel:

1. Builds its own widgets in the constructor.
2. Wires action listeners to a single `Action` per button.
3. Delegates to the service layer and renders the result via a `TableModel`.

There is no event bus and no observer plumbing across panels — when one panel
mutates state that another panel cares about, the parent frame is responsible for
refreshing both. This is verbose but easy to follow and easy to test.

## Persistence

The current implementation persists to a single text catalogue file under the
user's home directory. This is hidden behind `BookRepository`, so swapping it
for a SQLite or in-memory implementation is a matter of providing an alternative
implementation and a different binding in the application root.

## Testing strategy

- Domain types: pure unit tests for invariant enforcement.
- Service layer: unit tests against an in-memory `BookRepository` fake.
- UI layer: small smoke tests that instantiate panels with stub services and
  assert the rendered table model.

The CI pipeline runs all three suites on every push.
