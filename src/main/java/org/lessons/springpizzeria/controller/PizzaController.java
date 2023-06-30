package org.lessons.springpizzeria.controller;

import jakarta.validation.Valid;
import org.lessons.springpizzeria.messages.AlertMessage;
import org.lessons.springpizzeria.messages.AlertMessageType;
import org.lessons.springpizzeria.model.Pizza;
import org.lessons.springpizzeria.repository.IngredientRepository;
import org.lessons.springpizzeria.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/pizzas")
public class PizzaController {
    // dipende da PizzaRepository
    @Autowired
    private PizzaRepository pizzaRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

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
//        Optional<Pizza> result = pizzaRepository.findById(pizzaId);
//        if (result.isPresent()) {
//            model.addAttribute("pizza", result.get());
//        } else {
//            // ritorno http status 404
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza with id " + pizzaId + " not found");
//        }
        Pizza pizza = getPizzaById(pizzaId);
        // passa la pizza alla view
        model.addAttribute("pizza", pizza);
        //ritorna template
        return "/pizzas/detailPizza";
    }


    //  METODI CREATE
    // controller che restituisce la pagina con il form di creazione della nuova pizza
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("pizza", new Pizza());
        // aggiungo al model la lista degli ingrerdienti per popolare le checkbox
        model.addAttribute("ingredientList", ingredientRepository.findAll());
//        return "/pizzas/create"; // template del form di creazione pizza
        return "/pizzas/edit";
    }

    // controller che gestisce la post del form con i dati della nuova pizza
    @PostMapping("/create")
    public String store(@Valid @ModelAttribute("pizza") Pizza formPizza,
                        BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        // i dati della pizza sono dentro formPizza
        // verifico se ci sono errori
        if (bindingResult.hasErrors()) {
            // aggiungo al model la lista degli ingrerdienti per popolare le checkbox
            model.addAttribute("ingredientList", ingredientRepository.findAll());

            return "/pizzas/edit"; // ritorno il tamplate del form con la pizza precaricata
        }
        // setto timestamp di creazione
        formPizza.setCreatedAt(LocalDateTime.now());
        // metodo save salve se non esiste altrimenti fa update
        pizzaRepository.save(formPizza);


        // se tutto va a buon fine ritorna la lista delle pizze
        redirectAttributes.addFlashAttribute("message",
                new AlertMessage(AlertMessageType.SUCCESS, "Pizza created!"));
        return "redirect:/pizzas";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        // verifica se esiste pizza con quel id
//        Optional<Pizza> result = pizzaRepository.findById(id);
//        // se non esiste error 404
//        if (result.isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza with id " + id + "not found");
//        }
        Pizza pizza = getPizzaById(id);
        //recupero dati da DB
        // aggiungo pizza al model
        model.addAttribute("pizza", pizza);
        // aggiungo al model la lista degli ingrerdienti per popolare le checkbox
        model.addAttribute("ingredientList", ingredientRepository.findAll());
        //restituisco il template edit
        return "pizzas/edit";

    }

    @PostMapping("/edit/{id}")
    public String doEdit(@PathVariable Integer id,
                         @Valid @ModelAttribute("pizza") Pizza formPizza,
                         BindingResult bindingresult, RedirectAttributes redirectAttributes, Model model) {
        // cerco pizza da id
        Pizza pizzaToEdit = getPizzaById(id); // vecchia versione di pizza
        // valido formPizza
        if (bindingresult.hasErrors()) {
            // aggiungo al model la lista degli ingrerdienti per popolare le checkbox
            model.addAttribute("ingredientList", ingredientRepository.findAll());
            // se ci sono errori ritorno il template con il form
            return "/pizzas/edit";
        }
        // trasferisco su formPizza tutti i valori dei campi che non sono presenti nel form (altrimenti li perdo)
        formPizza.setId(pizzaToEdit.getId());
        formPizza.setCreatedAt(pizzaToEdit.getCreatedAt());
        // salvo i dati
        pizzaRepository.save(formPizza);
        redirectAttributes.addFlashAttribute("message",
                new AlertMessage(AlertMessageType.SUCCESS, "Pizza updated!"));
        return "redirect:/pizzas";
    }

    // METHODS DELETE

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        Pizza pizzaToDelete = getPizzaById(id);
        // cancello la pizza
        pizzaRepository.delete(pizzaToDelete);
        // add messaggio di successo come flashattribute
        redirectAttributes.addFlashAttribute("message",

                new AlertMessage(AlertMessageType.SUCCESS, pizzaToDelete.getName() + " deleted!"));
        return "redirect:/pizzas";

    }

    //UTILITY METHODS
    // metodo per selezionare una pizza da DB o eccezione
    private Pizza getPizzaById(Integer id) {
        // verificare se c'è una pizza con quell' id
        Optional<Pizza> result = pizzaRepository.findById(id);
        // se non esiste error 404
        if (result.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza with id " + id + "not found");
        }
        return result.get();
    }
}
