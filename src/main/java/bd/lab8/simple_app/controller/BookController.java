package bd.lab8.simple_app.controller;

import bd.lab8.simple_app.model.BatchPrint;
import bd.lab8.simple_app.model.Book;
import bd.lab8.simple_app.service.BookService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {

    private static final String REDIRECT = "redirect:/books";

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public String showBooksPage(@RequestParam(value = "page", defaultValue = "1") int page,
                                @RequestParam(value = "size", defaultValue = "10") int size,
                                @RequestParam(value = "sort", defaultValue = "name") String sort,
                                @RequestParam(value = "direction", defaultValue = "asc") String direction,
                                @RequestParam(value = "searchQuery", required = false) String searchQuery,
                                Model model) {

        Page<Book> bookPage = bookService.findAllBooks(page, size, sort, direction, searchQuery);

        model.addAttribute("books", bookPage.getContent());
        model.addAttribute("size", size);
        model.addAttribute("page", size);
        model.addAttribute("currentPage", bookPage.getNumber() + 1);
        model.addAttribute("totalPages", bookPage.getTotalPages());
        model.addAttribute("totalItems", bookPage.getTotalElements());
        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("searchQuery", searchQuery);

        return "books";
    }

    @GetMapping("/create")
    public String showCreateBookForm(Model model) {
        model.addAttribute("newBook", new Book());
        model.addAttribute("authors", bookService.findAllAuthors());
        model.addAttribute("genres", bookService.findAllGenres());
        return "create_book";
    }

    @PostMapping("/create")
    public String createBook(@Valid @ModelAttribute("newBook") Book newBook,
                             BindingResult bindingResult,
                             @RequestParam(value = "author", required = false) Long authorId,
                             @RequestParam(value = "genre", required = false) Long genreId,
                             Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("authors", bookService.findAllAuthors());
            model.addAttribute("genres", bookService.findAllGenres());

            return "create_book";
        }

        bookService.createBook(newBook, authorId, genreId);
        return REDIRECT;
    }

    @GetMapping("/edit/{id}")
    public String showEditBookForm(@PathVariable("id") Long id, Model model) {
        Book book = bookService.findBookById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid book id:" + id));
        model.addAttribute("updBook", book);
        model.addAttribute("authors", bookService.findAllAuthors());
        model.addAttribute("genres", bookService.findAllGenres());
        return "edit_book";
    }

    @PostMapping("/edit/{id}")
    public String updateBook(@Valid @ModelAttribute("updBook") Book updBook,
                             BindingResult bindingResult,
                             @PathVariable("id") Long id,
                             @RequestParam("author") Long authorId,
                             @RequestParam("genre") Long genreId,
                             Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("authors", bookService.findAllAuthors());
            model.addAttribute("genres", bookService.findAllGenres());

            return "edit_book";
        }

        try {
            bookService.updateBook(id, updBook, authorId, genreId);
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

        return REDIRECT;
    }

    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id) {
        bookService.deleteBook(id);
        return REDIRECT;
    }
}
