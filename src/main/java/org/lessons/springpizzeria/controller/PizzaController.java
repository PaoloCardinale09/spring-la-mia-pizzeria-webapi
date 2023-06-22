package org.lessons.springpizzeria.controller;

import org.lessons.springpizzeria.model.Pizza;
import org.lessons.springpizzeria.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/pizzas")
public class PizzaController {
    // dipende da PizzaRepository
    @Autowired
    private PizzaRepository pizzaRepository;

    @GetMapping
    public String list(Model model) { // è la mappa di attributi che il controller passa alla view
        // recuperare lista libri db
        List<Pizza> pizzas = pizzaRepository.findAll();
        // passo lista di pizza alla view
        model.addAttribute("pizzasList", pizzas);
        // restituisco il nome del template alla view
        return "/pizzas/list";
    }
}
