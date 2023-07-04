package org.lessons.springpizzeria.api;

import jakarta.validation.Valid;
import org.lessons.springpizzeria.model.Pizza;
import org.lessons.springpizzeria.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("api/v1/pizzas")
public class PizzaRestController {
    // controller per la risorsa pizza
    @Autowired
    private PizzaRepository pizzaRepository;

    //servizio per avere la lista di tutte le pizze
    @GetMapping
    public List<Pizza> index() {
        return pizzaRepository.findAll();

    }

    // Singola pizza
    @GetMapping("/{id}")
    public Pizza get(@PathVariable Integer id) {
        Optional<Pizza> pizza = pizzaRepository.findById(id);
        if (pizza.isPresent()) {
            return pizza.get();

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    // servizio per creare nuova pizza che mi arriva come json nel request body
    @PostMapping
    public Pizza create(@Valid @RequestBody Pizza pizza, BindingResult bindingResult) {

        return pizzaRepository.save(pizza);
    }

    // servizio per cancellare
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        pizzaRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public Pizza update(@PathVariable Integer id, @RequestBody Pizza pizza) {
        pizza.setId(id);
        return pizzaRepository.save(pizza);
    }


}
