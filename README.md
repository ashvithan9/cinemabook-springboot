# 🎬 CinemaBook — Cinema Booking System

A Spring Boot web app for booking cinema tickets. Covers user management, movies, shows, bookings, payments, and feedback. Uses CSV files as the data store instead of a database.

---

## 🛠 Tech Stack

- **Java 17**
- **Spring Boot 3.2.5**
- **Thymeleaf** (HTML templating)
- **CSV files** (data storage)
- **Lombok** (optional, used in some modules)
- **Maven**

---

## 📁 Project Structure

```
cinemabook/
├── src/main/java/com/cinemabook/
│   ├── CinemaBookApplication.java
│   ├── HomeController.java
│   ├── user/
│   ├── movie/
│   ├── show/
│   ├── booking/
│   ├── payment/
│   └── feedback/
├── src/main/resources/
│   ├── application.properties
│   └── templates/
│       ├── index.html
│       ├── user/
│       ├── movie/
│       ├── show/
│       ├── booking/
│       ├── payment/
│       └── feedback/
├── data/               
│   ├── users.csv
│   ├── movies.csv
│   ├── shows.csv
│   ├── bookings.csv
│   ├── payments.csv
│   └── feedback.csv
├── pom.xml
└── .gitignore
```

---

## 🚀 Getting Started

### Prerequisites
- Java 17 installed
- IntelliJ IDEA
- Git

### Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourname/cinemabook.git
   cd cinemabook
   ```

2. **Create the data folder** at the project root and add all CSV files with just their headers (see CSV Setup below)

3. **Open in IntelliJ** → Load Maven Project when prompted

4. **Run** `CinemaBookApplication.java`

5. **Visit** `http://localhost:8080`

---

## 🗂 CSV Setup

Create a `data/` folder at the project root and add these files:

**users.csv**
```
userId,name,email,password,phone,role
ADMIN001,Admin,admin@cinemabook.com,admin123,0000000000,ADMIN
```

**movies.csv**
```
movieId,title,genre,duration,rating,description,imageUrl,status
```

**shows.csv**
```
showId,movieId,date,time,hall,totalSeats,availableSeats
```

**bookings.csv**
```
bookingId,userId,showId,seats,bookingDate,status
```

**payments.csv**
```
paymentId,userId,bookingId,amount,status,paymentDate
```

**feedback.csv**
```
feedbackId,userId,movieId,rating,comment,date
```

---

## 🔐 Default Admin Account

```
Email:    admin@cinemabook.com
Password: admin123
```

> ⚠️ The admin account is hardcoded in `users.csv`. New registrations always create a CUSTOMER account — no one can register as admin.

---

## 👥 Module Ownership

| Module | Owner |
|---|---|
| User & Auth | — |
| Movie | — |
| Show | — |
| Booking | — |
| Payment | — |
| Feedback | — |

---

## 🌿 Git Workflow — READ BEFORE YOU COMMIT

> ⚠️ **Never commit directly to `main`. No exceptions.**

### Step 1 — Always create your own branch first

```bash
git checkout -b feature/your-module-name
```

Examples:
```bash
git checkout -b feature/payment
git checkout -b feature/show
git checkout -b feature/feedback
```

### Step 2 — Make your changes, then stage and commit

```bash
git add .
git commit -m "Add payment module - model, service, controller and templates"
```

### Step 3 — Push your branch to GitHub

```bash
git push origin feature/your-module-name
```

### Step 4 — Open a Pull Request on GitHub

- Go to the repository on GitHub
- Click **Compare & pull request**
- Add a short description of what you built
- Request a review from the team lead
- **Do not merge your own PR**

### Step 5 — After your PR is merged, clean up

```bash
git checkout main
git pull origin main
git branch -d feature/your-module-name
```

---

## 🔁 Keeping Your Branch Up to Date

Before starting work each day, sync with main:

```bash
git checkout main
git pull origin main
git checkout feature/your-module-name
git merge main
```

This prevents merge conflicts from building up.

---

## ⚠️ Rules

- ❌ Do not push to `main` directly
- ❌ Do not merge your own Pull Request
- ❌ Do not edit another person's module files without asking
- ✅ One branch per module
- ✅ Commit messages should describe what you did
- ✅ Always pull from main before starting new work

---

## 🔗 Module Dependencies

Build order matters. Each module depends on the ones above it:

```
Movie ──────────────────────────────┐
User ───────────────────────────┐   │
                                ↓   ↓
                              Show ←─┘
                                ↓
                            Booking
                           ↙       ↘
                       Payment   Feedback
```

- **Show** needs `movieId` from Movie
- **Booking** needs `userId` from User and `showId` from Show
- **Payment** needs `bookingId` from Booking and `userId` from User
- **Feedback** needs `userId` from User and `movieId` from Movie

---

## 💡 CSV Path Convention

All services must use this path format — do not use relative paths:

```java
private static final String CSV_PATH = System.getProperty("user.dir") + "/data/movies.csv";
```

Replace `movies.csv` with your module's CSV file name.

---

## 🐛 Common Issues

**App won't start**
- Check `pom.xml` has `spring-boot-starter-web` not `spring-boot-starter-webmvc`
- Spring Boot version must be `3.2.5`

**CSV not updating**
- Make sure your CSV path uses `System.getProperty("user.dir")`
- CSV files must be in the `data/` folder at the project root, not inside `src/main/resources`

**IntelliJ keeps shutting down**
- Go to Help → Change Memory Settings → set to 2048m
- Add `spring.devtools.restart.enabled=false` to `application.properties`

**Template not found error**
- Check the return string in your controller matches the exact file path under `templates/`
- Example: `return "movie/list"` needs `templates/movie/list.html` to exist
