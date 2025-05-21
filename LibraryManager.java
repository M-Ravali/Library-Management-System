import java.util.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

class Book {
    String id, title, author;
    boolean isAvailable;
    String borrower;
    LocalDate dueDate;

    Book(String id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isAvailable = true;
        this.borrower = null;
        this.dueDate = null;
    }

    public String toString() {
        return id + " - " + title + " by " + author + " [" + (isAvailable ? "Available" : "Borrowed by " + borrower + ", due: " + dueDate) + "]";
    }
}

class User {
    String username, password;

    User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

public class LibraryManager {
    static List<Book> books = new ArrayList<>();
    static Map<String, User> users = new HashMap<>();
    static String currentUser = null;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        loadSampleBooks();
        loadSampleUsers();
        while (true) {
            System.out.println("\n1. Register\n2. Login\n3. Exit");
            int option = sc.nextInt(); sc.nextLine();
            switch (option) {
                case 1 -> registerUser();
                case 2 -> {
                    if (login()) runLibrary();
                }
                case 3 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    static void registerUser() {
        System.out.print("Enter username: ");
        String username = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();
        users.put(username, new User(username, password));
        System.out.println("Registration successful.");
    }

    static boolean login() {
        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();
        if (users.containsKey(username) && users.get(username).password.equals(password)) {
            currentUser = username;
            System.out.println("Login successful.");
            return true;
        }
        System.out.println("Login failed.");
        return false;
    }

    static void runLibrary() {
        while (true) {
            System.out.println("\n1. View Books\n2. Search Book\n3. Borrow Book\n4. Return Book\n5. Logout");
            int choice = sc.nextInt(); sc.nextLine();
            switch (choice) {
                case 1 -> viewBooks();
                case 2 -> searchBooks();
                case 3 -> borrowBook();
                case 4 -> returnBook();
                case 5 -> {
                    currentUser = null;
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    static void loadSampleBooks() {
        books.add(new Book("101", "Java Basics", "John Smith"));
        books.add(new Book("102", "OOP in Java", "Jane Doe"));
        books.add(new Book("103", "Data Structures", "Mark Brown"));
    }

    static void loadSampleUsers() {
        users.put("admin", new User("admin", "admin123"));
    }

    static void viewBooks() {
        System.out.println("\nLibrary Books:");
        for (Book book : books) {
            System.out.println(book);
        }
    }

    static void searchBooks() {
        System.out.print("Enter keyword (title/author): ");
        String keyword = sc.nextLine().toLowerCase();
        System.out.println("Search Results:");
        for (Book book : books) {
            if (book.title.toLowerCase().contains(keyword) || book.author.toLowerCase().contains(keyword)) {
                System.out.println(book);
            }
        }
    }

    static void borrowBook() {
        System.out.print("Enter book ID to borrow: ");
        String id = sc.nextLine();
        for (Book book : books) {
            if (book.id.equals(id) && book.isAvailable) {
                book.isAvailable = false;
                book.borrower = currentUser;
                book.dueDate = LocalDate.now().plusDays(14);
                System.out.println("You borrowed: " + book.title + ". Due on: " + book.dueDate);
                return;
            }
        }
        System.out.println("Book not available or not found.");
    }

    static void returnBook() {
        System.out.print("Enter book ID to return: ");
        String id = sc.nextLine();
        for (Book book : books) {
            if (book.id.equals(id) && !book.isAvailable && book.borrower.equals(currentUser)) {
                long overdue = ChronoUnit.DAYS.between(book.dueDate, LocalDate.now());
                book.isAvailable = true;
                book.borrower = null;
                book.dueDate = null;
                System.out.println("Book returned.");
                if (overdue > 0) {
                    System.out.println("You have a fine of $" + overdue);
                }
                return;
            }
        }
        System.out.println("You cannot return this book.");
    }
}
