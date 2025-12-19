package pl.edu.wszib.librarymanager.gui;

import org.springframework.stereotype.Component;
import pl.edu.wszib.librarymanager.model.Book;
import pl.edu.wszib.librarymanager.model.Role;
import pl.edu.wszib.librarymanager.model.User;
import pl.edu.wszib.librarymanager.service.IAuthService;
import pl.edu.wszib.librarymanager.service.IBookService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class GUI implements IGUI {

    private final IAuthService authService;
    private final IBookService bookService;
    private final Scanner scanner = new Scanner(System.in);

    public GUI(IAuthService authService, IBookService bookService) {
        this.authService = authService;
        this.bookService = bookService;
    }

    @Override
    public void start() {
        while (true) {
            Optional<User> maybeUser = loginLoop();
            if (maybeUser.isEmpty()) {
                System.out.println("Koniec programu.");
                return;
            }
            User user = maybeUser.get();
            System.out.println("\nZalogowano jako: " + user.getUsername() + " (" + user.getRole() + ")\n");

            if (user.getRole() == Role.ADMIN) {
                adminMenuLoop();
            } else {
                userMenuLoop();
            }
        }
    }

    private Optional<User> loginLoop() {
        System.out.println("=== Terminalowy Menedżer Biblioteki ===");
        System.out.println("(Wpisz 'q' jako login aby wyjść)\n");

        while (true) {
            System.out.print("Login: ");
            String login = scanner.nextLine().trim();
            if (login.equalsIgnoreCase("q")) {
                return Optional.empty();
            }
            System.out.print("Hasło: ");
            String password = scanner.nextLine();

            Optional<User> user = authService.login(login, password);
            if (user.isPresent()) {
                return user;
            }
            System.out.println("Błędny login lub hasło. Spróbuj ponownie.\n");
        }
    }

    private void userMenuLoop() {
        while (true) {
            System.out.println("=== MENU USER ===");
            System.out.println("1. Lista książek");
            System.out.println("2. Szukaj po tytule");
            System.out.println("3. Szukaj po autorze");
            System.out.println("0. Wyloguj");
            System.out.print("> ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> showBooks(bookService.listAll());
                case "2" -> {
                    System.out.print("Fraza (tytuł): ");
                    showBooks(bookService.searchByTitle(scanner.nextLine()));
                }
                case "3" -> {
                    System.out.print("Fraza (autor): ");
                    showBooks(bookService.searchByAuthor(scanner.nextLine()));
                }
                case "0" -> {
                    System.out.println("Wylogowano.\n");
                    return;
                }
                default -> System.out.println("Nieznana opcja.\n");
            }
        }
    }

    private void adminMenuLoop() {
        while (true) {
            System.out.println("=== MENU ADMIN ===");
            System.out.println("1. Lista książek");
            System.out.println("2. Szukaj po tytule");
            System.out.println("3. Szukaj po autorze");
            System.out.println("4. Dodaj książkę");
            System.out.println("5. Edytuj książkę");
            System.out.println("6. Usuń książkę");
            System.out.println("0. Wyloguj");
            System.out.print("> ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> showBooks(bookService.listAll());
                case "2" -> {
                    System.out.print("Fraza (tytuł): ");
                    showBooks(bookService.searchByTitle(scanner.nextLine()));
                }
                case "3" -> {
                    System.out.print("Fraza (autor): ");
                    showBooks(bookService.searchByAuthor(scanner.nextLine()));
                }
                case "4" -> addBook();
                case "5" -> editBook();
                case "6" -> deleteBook();
                case "0" -> {
                    System.out.println("Wylogowano.\n");
                    return;
                }
                default -> System.out.println("Nieznana opcja.\n");
            }
        }
    }

    private void addBook() {
        System.out.print("Tytuł: ");
        String title = scanner.nextLine().trim();
        System.out.print("Autor: ");
        String author = scanner.nextLine().trim();

        if (title.isEmpty() || author.isEmpty()) {
            System.out.println("Tytuł i autor nie mogą być puste.\n");
            return;
        }

        Book created = bookService.add(title, author);
        System.out.println("Dodano książkę: " + format(created) + "\n");
    }

    private void editBook() {
        Long id = readLong("ID książki do edycji: ");
        if (id == null) {
            return;
        }

        Optional<Book> existing = bookService.get(id);
        if (existing.isEmpty()) {
            System.out.println("Nie znaleziono książki o ID " + id + ".\n");
            return;
        }

        Book b = existing.get();
        System.out.println("Aktualnie: " + format(b));

        System.out.print("Nowy tytuł (enter = bez zmian): ");
        String title = scanner.nextLine();
        System.out.print("Nowy autor (enter = bez zmian): ");
        String author = scanner.nextLine();

        String newTitle = title.isBlank() ? b.getTitle() : title.trim();
        String newAuthor = author.isBlank() ? b.getAuthor() : author.trim();

        boolean ok = bookService.update(id, newTitle, newAuthor);
        System.out.println(ok ? "Zapisano zmiany.\n" : "Nie udało się zapisać zmian.\n");
    }

    private void deleteBook() {
        Long id = readLong("ID książki do usunięcia: ");
        if (id == null) {
            return;
        }
        boolean ok = bookService.delete(id);
        System.out.println(ok ? "Usunięto książkę.\n" : "Nie znaleziono książki o podanym ID.\n");
    }

    private void showBooks(List<Book> books) {
        System.out.println();
        if (books.isEmpty()) {
            System.out.println("Brak wyników.\n");
            return;
        }
        System.out.println("ID | Autor | Tytuł");
        System.out.println("---+-------+------");
        for (Book b : books) {
            System.out.println(b.getId() + " | " + b.getAuthor() + " | " + b.getTitle());
        }
        System.out.println();
    }

    private String format(Book b) {
        return "[" + b.getId() + "] " + b.getAuthor() + " - " + b.getTitle();
    }

    private Long readLong(String prompt) {
        System.out.print(prompt);
        String txt = scanner.nextLine().trim();
        try {
            return Long.parseLong(txt);
        } catch (NumberFormatException e) {
            System.out.println("To nie jest poprawne ID.\n");
            return null;
        }
    }
}
