package pl.edu.wszib.librarymanager.service;

import org.springframework.stereotype.Service;
import pl.edu.wszib.librarymanager.model.Book;
import pl.edu.wszib.librarymanager.repository.IBookRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BookService implements IBookService {

    private final IBookRepository bookRepository;

    public BookService(IBookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> listAll() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> searchByTitle(String phrase) {
        return bookRepository.findByTitleContains(phrase);
    }

    @Override
    public List<Book> searchByAuthor(String phrase) {
        return bookRepository.findByAuthorContains(phrase);
    }

    @Override
    public Book add(String title, String author) {
        return bookRepository.add(title, author);
    }

    @Override
    public boolean delete(long id) {
        return bookRepository.deleteById(id);
    }

    @Override
    public boolean update(long id, String newTitle, String newAuthor) {
        Optional<Book> existing = bookRepository.findById(id);
        if (existing.isEmpty()) {
            return false;
        }
        Book b = existing.get();
        b.setTitle(newTitle);
        b.setAuthor(newAuthor);
        return bookRepository.update(b);
    }

    @Override
    public Optional<Book> get(long id) {
        return bookRepository.findById(id);
    }
}
