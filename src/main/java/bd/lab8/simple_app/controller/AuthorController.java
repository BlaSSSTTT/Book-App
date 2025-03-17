package bd.lab8.simple_app.controller;

import bd.lab8.simple_app.dto.AuthorDto;
import bd.lab8.simple_app.service.AuthorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/top-and-bottom")
    public String showTopAndBottomAuthors(@RequestParam("genreId") int genreId, Model model) {
        List<List<AuthorDto>> results = authorService.executeTopAndBottomSuccessfulAuthors(genreId);

        // Передача таблиць у модель
        model.addAttribute("topAuthors", results.get(0));
        model.addAttribute("bottomAuthors", results.get(1));

        return "authors";
    }
}
