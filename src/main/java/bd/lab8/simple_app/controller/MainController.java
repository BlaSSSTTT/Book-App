package bd.lab8.simple_app.controller;

import bd.lab8.simple_app.model.Genre;
import bd.lab8.simple_app.repository.GenreRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class MainController {

    private final GenreRepository genreRepository;

    public MainController(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @GetMapping("/")
    public String mainPage(Model model) {
        List<Genre> genres = genreRepository.findAll();
        model.addAttribute("genres", genres);

        return "main";
    }
}
