# ICE-4---Administrator-and-Common-users
# 🔒 Data Centre Security System

A Java Swing desktop application simulating a smart security system for a data centre. Built as part of an ICE (In-Class Exercise) assignment covering core Java concepts including control structures, ArrayLists, switch statements, and Java Date/Time.

---

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Project Structure](#project-structure)
- [How to Run](#how-to-run)
- [Test Accounts](#test-accounts)
- [How It Works](#how-it-works)
- [Java Concepts Covered](#java-concepts-covered)
- [File Breakdown](#file-breakdown)

---

## Overview

The system controls user access to a data centre by managing login credentials, user roles, and account lockouts. It features a dark-themed JFrame GUI and supports three user roles: Admin, Staff, and Visitor — each with different levels of access.

---

## Features

| Feature | Description |
|---|---|
| 🔐 Login System | Username + PIN authentication with attempt tracking |
| 🔒 Account Lockout | Locks after 3 failed attempts with a 3-minute cooldown timer |
| 👤 Role-Based Access | Admin, Staff, and Visitor menus using a switch statement |
| ➕ Create Users | Admin can add new users stored in parallel ArrayLists |
| 📋 View Users | Admin can view all users, roles, status, and creation timestamps |
| 🕐 Timestamps | Every user account records the exact date and time it was created |
| ✅ Duplicate Check | Prevents two users from having the same username |

---

## Project Structure

```
SecuritySystem/
│
├── Main.java             # Entry point — launches the Login screen
├── DataStore.java        # Shared data layer — all parallel ArrayLists live here
├── LoginFrame.java       # Login window with attempt counter and lockout timer
├── MenuFrame.java        # Role-based menu (switch statement)
├── CreateUserFrame.java  # Admin-only: create new users
└── ViewUsersFrame.java   # Admin-only: view all users in a table
```

---

## How to Run

### Requirements
- Java JDK 8 or higher
- Any Java IDE (IntelliJ IDEA, Eclipse, NetBeans) or the command line

### Option A — Command Line

```bash
# 1. Navigate to the project folder
cd path/to/SecuritySystem

# 2. Compile all files
javac *.java

# 3. Run the application
java Main
```

### Option B — IDE (IntelliJ / Eclipse)

1. Create a new Java project
2. Copy all `.java` files into the `src` folder
3. Right-click `Main.java` → **Run**

> ⚠️ All `.java` files must be in the **same folder/package** for the imports to work correctly.

---

## Test Accounts

These accounts are pre-loaded when the app starts:

| Username | PIN  | Role    |
|----------|------|---------|
| admin    | 1234 | Admin   |
| manman    | 2222 | Staff   |
| taeg      | 3333 | Staff   |
| me  | 4444 | Visitor |

---

## How It Works

### Login Flow
1. User enters username and PIN
2. System checks credentials against the `DataStore` ArrayLists
3. On failure: increments fail counter — after **3 failures**, account is locked
4. Lockout timer runs for **3 minutes** in the background, then automatically unlocks
5. On success: opens the role-appropriate menu

### Role Access (switch statement)
```
Role 1 → Admin   → Can create users + view all users
Role 2 → Staff   → Sees staff access confirmation
Role 3 → Visitor → Sees visitor access confirmation
```

### Parallel ArrayList Structure
All user data is stored across 6 lists that share the same index:

```
Index:       0         1       2       3
usernames: [admin,   manman,   taeg,  me]
pins:      [1234,    2222,   3333,   4444  ]
roles:     [1,       2,      2,      3     ]
timestamps:[...,     ...,    ...,    ...   ]
locked:    [false,   false,  false,  false ]
failCounts:[0,       0,      0,      0     ]
```

So `index 1` always refers to "alice" across every list.

---

## Java Concepts Covered

| Concept | Where Used |
|---|---|
| `if / if-else / nested if` | `LoginFrame.java` — credential and lockout checks |
| Logical operators (`&&`, `\|\|`) | `CreateUserFrame.java` — input validation |
| `switch` statement | `MenuFrame.java` — role-based menus; `DataStore.roleName()` |
| `ArrayList` | `DataStore.java` — all 6 parallel lists |
| `java.time.LocalDateTime` | `DataStore.addUser()` — timestamps on creation |
| `java.util.Timer` + `TimerTask` | `LoginFrame.startLockoutTimer()` — 3-min lockout |
| `SwingUtilities.invokeLater` | `LoginFrame.java` — safe UI updates from background thread |
| `JFrame / JPanel / JTable` | All GUI files — window and layout management |
| Inheritance (`extends JFrame`) | All Frame classes |
| Static fields and methods | `DataStore.java` — shared global state |

---

## File Breakdown

### `DataStore.java`
The central data store. All fields are `static` so every class shares the same instance. Contains the pre-populated user list and helper methods `addUser()`, `findUser()`, and `roleName()`.

### `Main.java`
The entry point. Launches `LoginFrame` safely on the Swing Event Dispatch Thread (EDT) using `SwingUtilities.invokeLater()`.

### `LoginFrame.java`
The first screen. Handles username/PIN input, tracks failed attempts per user, locks accounts after 3 failures, and starts a `java.util.Timer` for the 3-minute cooldown. On success, opens `MenuFrame` and closes itself.

### `MenuFrame.java`
Shown after login. Uses a `switch(role)` statement to build a different button layout depending on whether the user is Admin, Staff, or Visitor. Admin gets access to Create User and View Users screens.

### `CreateUserFrame.java`
Admin-only. Validates input with logical operators, checks for duplicate usernames, then calls `DataStore.addUser()` which appends to all 6 parallel ArrayLists simultaneously and records a `LocalDateTime` timestamp.

### `ViewUsersFrame.java`
Admin-only. Iterates all parallel ArrayLists with a single `for` loop index and displays every user in a styled `JTable`. Includes a refresh button to reload the data.

---

## Extension Features Implemented

- ✅ **Lockout Timer** — 3-minute automatic unlock using `java.util.Timer`
- ✅ **Duplicate Username Prevention** — checked before adding any new user
