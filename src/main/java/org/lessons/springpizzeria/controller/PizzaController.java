package org.lessons.springpizzeria.controller;

import org.lessons.springpizzeria.model.Pizza;
import org.lessons.springpizzeria.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza with id " + pizzaId + "not found");
        }
        //ritorna template
        return "/pizzas/detailPizza";
    }
}
