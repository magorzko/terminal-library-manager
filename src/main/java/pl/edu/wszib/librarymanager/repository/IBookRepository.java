package pl.edu.wszib.librarymanager.repository;

import pl.edu.wszib.librarymanager.model.Book;

import java.util.List;
import java.util.Optional;

public interface IBookRepository {
    List<Book> findAll();
    Optional<Book> findById(long id);
    List<Book> findByTitleContains(String phrase);
    List<Book> findByAuthorContains(String phrase);

    Book add(String title, String author);
    boolean deleteById(long id);
    boolean update(Book book);
}
