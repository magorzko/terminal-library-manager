package pl.edu.wszib.librarymanager.repository;

import org.springframework.stereotype.Repository;
import pl.edu.wszib.librarymanager.model.Book;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class InMemoryBookRepository implements IBookRepository {

    private final AtomicLong idSeq = new AtomicLong(0);
    private final List<Book> books = new ArrayList<>();

    public InMemoryBookRepository() {
        // seed danych (żeby było co przeglądać po starcie)
        add("Harry Potter i Kamień Filozoficzny", "J.K. Rowling");
        add("Harry Potter i Komnata Tajemnic", "J.K. Rowling");
        add("Harry Potter i Więzień Azkabanu", "J.K. Rowling");
        add("Harry Potter i Czara Ognia", "J.K. Rowling");

        add("Władca Pierścieni: Drużyna Pierścienia", "J.R.R. Tolkien");
        add("Władca Pierścieni: Dwie Wieże", "J.R.R. Tolkien");
        add("Władca Pierścieni: Powrót Króla", "J.R.R. Tolkien");

        add("Hobbit", "J.R.R. Tolkien");
    }

    @Override
    public List<Book> findAll() {
        return books.stream()
                .sorted(Comparator.comparingLong(Book::getId))
                .map(b -> Book.builder().id(b.getId()).title(b.getTitle()).author(b.getAuthor()).build())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Book> findById(long id) {
        return books.stream()
                .filter(b -> b.getId() == id)
                .findFirst()
                .map(b -> Book.builder().id(b.getId()).title(b.getTitle()).author(b.getAuthor()).build());
    }

    @Override
    public List<Book> findByTitleContains(String phrase) {
        String p = normalize(phrase);
        return books.stream()
                .filter(b -> normalize(b.getTitle()).contains(p))
                .map(b -> Book.builder().id(b.getId()).title(b.getTitle()).author(b.getAuthor()).build())
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findByAuthorContains(String phrase) {
        String p = normalize(phrase);
        return books.stream()
                .filter(b -> normalize(b.getAuthor()).contains(p))
                .map(b -> Book.builder().id(b.getId()).title(b.getTitle()).author(b.getAuthor()).build())
                .collect(Collectors.toList());
    }

    @Override
    public Book add(String title, String author) {
        Book book = Book.builder()
                .id(idSeq.incrementAndGet())
                .title(title)
                .author(author)
                .build();
        books.add(book);
        return Book.builder().id(book.getId()).title(book.getTitle()).author(book.getAuthor()).build();
    }

    @Override
    public boolean deleteById(long id) {
        return books.removeIf(b -> b.getId() == id);
    }

    @Override
    public boolean update(Book book) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId() == book.getId()) {
                books.set(i, Book.builder()
                        .id(book.getId())
                        .title(book.getTitle())
                        .author(book.getAuthor())
                        .build());
                return true;
            }
        }
        return false;
    }

    private String normalize(String s) {
        return s == null ? "" : s.trim().toLowerCase();
    }
}
