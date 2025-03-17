package bd.lab8.simple_app.service;

import bd.lab8.simple_app.model.Author;
import bd.lab8.simple_app.model.Book;
import bd.lab8.simple_app.model.Genre;
import bd.lab8.simple_app.repository.AuthorRepository;
import bd.lab8.simple_app.repository.BookRepository;
import bd.lab8.simple_app.repository.GenreRepository;
import bd.lab8.simple_app.repository.OrderBookRepository;
import bd.lab8.simple_app.transaction.TransactionManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final OrderBookRepository orderBookRepository;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    public BookService(OrderBookRepository orderBookRepository, BookRepository bookRepository,
                       AuthorRepository authorRepository,
                       GenreRepository genreRepository) {
        this.orderBookRepository = orderBookRepository;
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
    }

    @Transactional
    public Page<Book> findAllBooks(int page, int size, String sort, String direction, String searchQuery) {
        size = Math.clamp(size, 1, 100);
        page = Math.max(page - 1, 0);

        Sort sortOrder = direction.equals("asc") ? Sort.by(sort).ascending() : Sort.by(sort).descending();
        Pageable pageable = PageRequest.of(page, size, sortOrder);

        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            return bookRepository.findAll(pageable);
        }

        return bookRepository.findByNameContainingIgnoreCase(searchQuery.trim(), pageable);
    }

    @Transactional
    public Optional<Book> findBookById(Long id) {
        return bookRepository.findById(id);
    }

    @Transactional
    public void createBook(Book book, Long authorId, Long genreId) {
        Optional<Author> authorOpt = authorRepository.findById(authorId);
        Optional<Genre> genreOpt = genreRepository.findById(genreId);

        if (authorOpt.isPresent() && genreOpt.isPresent()) {
            book.setAuthor(authorOpt.get());
            book.setGenre(genreOpt.get());
            bookRepository.save(book);
        } else {
            throw new IllegalArgumentException("Invalid Author ID or Genre ID");
        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void updateBook(Long id, Book updatedBook, Long authorId, Long genreId) {
        try {
            Optional<Book> existingBookOpt = bookRepository.findById(id);

            if (existingBookOpt.isPresent()) {
                Book existingBook = existingBookOpt.get();
                updateBookFields(existingBook, updatedBook);

                authorRepository.findById(authorId).ifPresent(existingBook::setAuthor);
                genreRepository.findById(genreId).ifPresent(existingBook::setGenre);

                bookRepository.save(existingBook);
            } else {
                throw new IllegalArgumentException("Book not found");
            }
        } catch (Exception e) {
            System.out.println("Another transaction is modifying this book. Please try again later.");
            throw new RuntimeException("Another transaction is modifying this book. Please try again later.", e);
        }

        TransactionManager.printIsolationLevel();
    }

    @Transactional
    public void deleteBook(Long id) {
        orderBookRepository.deleteByBookId(id);
        bookRepository.deleteById(id);
    }

    public List<Author> findAllAuthors() {
        return authorRepository.findAll();
    }

    public List<Genre> findAllGenres() {
        return genreRepository.findAll();
    }

    private void updateBookFields(Book existingBook, Book updatedBook) {
        updatedBook.setName(existingBook.getName());
        updatedBook.setAuthor(existingBook.getAuthor());
        updatedBook.setGenre(existingBook.getGenre());
        updatedBook.setPublicationYear(existingBook.getPublicationYear());
        updatedBook.setPublicationYear(existingBook.getPublicationYear());
        updatedBook.setSku(existingBook.getSku());
        updatedBook.setIsbn(existingBook.getIsbn());
        updatedBook.setPages(existingBook.getPages());
        updatedBook.setSize(existingBook.getSize());
        updatedBook.setWeight(existingBook.getWeight());
        updatedBook.setAnnotation(existingBook.getAnnotation());
    }
}
