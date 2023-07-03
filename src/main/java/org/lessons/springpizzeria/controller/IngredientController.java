package org.lessons.springpizzeria.controller;

import jakarta.validation.Valid;
import org.lessons.springpizzeria.model.Ingredient;
import org.lessons.springpizzeria.model.Pizza;
import org.lessons.springpizzeria.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/ingredients")
public class IngredientController {

    @Autowired
    private IngredientRepository ingredientRepository;

    @GetMapping
    public String index(Model model, @RequestParam("edit") Optional<Integer> ingredientId) {
        List<Ingredient> ingredientList = ingredientRepository.findAll();
        model.addAttribute("ingredients", ingredientList);

        Ingredient ingredientObj;
        // se ho il parametro ingredientId allora cerco l' ingrediente su database
        if (ingredientId.isPresent()) {
            Optional<Ingredient> ingredientDb = ingredientRepository.findById(ingredientId.get());
            // se è presente valorizzo ingredientObj con l'ingredient da db
            if (((Optional<?>) ingredientDb).isPresent()) {
                ingredientObj = ingredientDb.get();
            } else {
                // se non è presente valorizzo ingredientObj con un ingredient vuot
                ingredientObj = new Ingredient();
            }
        } else {
            // se non ho il parametro ingredintObj con una ingredient vuota
            ingredientObj = new Ingredient();
        }
        // passo al model un attributo categoryObj per mappare il form su un oggetto di tipo Category
        model.addAttribute("ingredientObj", ingredientObj);

        return "/ingredients/index";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("ingredientObj") Ingredient formIngredient,
                       BindingResult bindingResult, Model model) {
        // verifichiamo se ci sono errori
        if (bindingResult.hasErrors()) {
            model.addAttribute("ingredients", ingredientRepository.findAll());
            return "/ingredients/index";
        }
        // salvare l' ingredient
        ingredientRepository.save(formIngredient);
        // fa la redirect alla index
        return "redirect:/ingredients";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        // prima di eliminare gli ingredienti la dissocio da tutte le pizze
        Optional<Ingredient> result = ingredientRepository.findById(id);
        if (result.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        // ingredient che devo eliminare
        Ingredient ingredientToDelete = result.get();
        // per ogni pizza associata all' ingredient da eliminare
        for (Pizza pizza : ingredientToDelete.getPizzas()) {
            pizza.getIngredients().remove(ingredientToDelete);
        }

        ingredientRepository.deleteById(id);
        return "redirect:/ingredients";
    }
}