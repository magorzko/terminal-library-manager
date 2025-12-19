package pl.edu.wszib.librarymanager.service;

import pl.edu.wszib.librarymanager.model.Book;

import java.util.List;
import java.util.Optional;

public interface IBookService {
    List<Book> listAll();
    List<Book> searchByTitle(String phrase);
    List<Book> searchByAuthor(String phrase);

    Book add(String title, String author);
    boolean delete(long id);
    boolean update(long id, String newTitle, String newAuthor);
    Optional<Book> get(long id);
}
