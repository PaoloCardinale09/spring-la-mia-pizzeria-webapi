package org.lessons.springpizzeria.controller;

import jakarta.validation.Valid;
import org.lessons.springpizzeria.model.Pizza;
import org.lessons.springpizzeria.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/pizzas")
public class PizzaController {
    // dipende da PizzaRepository
    @Autowired
    private PizzaRepository pizzaRepository;

//    @GetMapping
//    public String list(Model model) { // è la mappa di attributi che il controller passa alla view
//        // recuperare lista pizza db
//        List<Pizza> pizzas = pizzaRepository.findAll();
//        // passo lista di pizza alla view
//        model.addAttribute("pizzasList", pizzas);
//        // restituisco il nome del template alla view
//        return "/pizzas/list";
//    }

    // metodo che può ricevere occasionalmente un paramentro, se c'è filtriamo le pizze, se non c'è ritorno tutto
    @GetMapping
    public String list(
            @RequestParam(name = "keyword", required = false) String searchingString, Model model) { // è la mappa di attributi che il controller passa alla view
        List<Pizza> pizzas;
        if (searchingString == null || searchingString.isBlank()) {
            pizzas = pizzaRepository.findAll();
        } else {
            pizzas = pizzaRepository.findByNameContainingIgnoreCase(searchingString);
        }
        // passo lista di pizza alla view
        model.addAttribute("pizzasList", pizzas);
        model.addAttribute("searchInput", searchingString == null ? "" : searchingString);
        // restituisco il nome del template alla view
        return "/pizzas/list";
    }

    @GetMapping("/{id}")
    public String pizzaDetail(@PathVariable("id") Integer pizzaId, Model model) {
        // cerca db dettaglio con id
        Optional<Pizza> result = pizzaRepository.findById(pizzaId);
        if (result.isPresent()) {
            model.addAttribute("pizza", result.get());
        } else {
            // ritorno http status 404
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza with id " + pizzaId + " not found");
        }
        //ritorna template
        return "/pizzas/detailPizza";
    }

    // controller che restituisce la pagina con il form di creazione della nuova pizza
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("pizza", new Pizza());
        return "/pizzas/create"; // template del form di creazione pizza
    }

    // controller che gestisce la post del form con i dati della nuova pizza
    @PostMapping("/create")
    public String store(@Valid @ModelAttribute("pizza") Pizza formPizza, BindingResult bindingResult) {
        // i dati della pizza sono dentro formPizza
        // verifico se ci sono errori
        if (bindingResult.hasErrors()) {
            return "/pizzas/create"; // ritorno il tamplate del form con la pizza precaricata
        }
        // setto timestamp di creazione
        formPizza.setCreatedAt(LocalDateTime.now());
        // metodo save salve se non esiste altrimenti fa update
        pizzaRepository.save(formPizza);

        // se tutto va a buon fine ritorna la lista delle pizze
        return "redirect:/pizzas";
    }
}
