package org.lessons.springpizzeria.controller;

import jakarta.validation.Valid;
import org.lessons.springpizzeria.model.Pizza;
import org.lessons.springpizzeria.model.SpecialOffer;
import org.lessons.springpizzeria.repository.PizzaRepository;
import org.lessons.springpizzeria.repository.SpecialOfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Controller
@RequestMapping("/specialOffers")
public class SpecialOfferController {
    @Autowired
    PizzaRepository pizzaRepository;

    @Autowired
    SpecialOfferRepository specialOfferRepository;

    @GetMapping("/create")
    public String create(@RequestParam("pizzaId") Integer pizzaId, Model model) {
        SpecialOffer specialOffer = new SpecialOffer();

        Optional<Pizza> pizza = pizzaRepository.findById(pizzaId);
        if (pizza.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza with id " + pizzaId + "not found");
        }
        specialOffer.setPizza(pizza.get());
        // aggiungo al model l'attributo specialOffer
        model.addAttribute("specialOffer", specialOffer);
        return "/specialOffers/form";
    }

    @PostMapping("/create")
    public String doCreate(@Valid @ModelAttribute("specialOffer") SpecialOffer formSpecialOffer, BindingResult bindingResult) {
        // valido
        if (bindingResult.hasErrors()) {
            //se ci sono errori ricreo il form
            return "specialOffers/form";
        }
        //se non ci sono errori salvo le special offers
        specialOfferRepository.save(formSpecialOffer);
        // faccio una redirect alla pagina di dettaglio della pizza
        return "redirect:/pizzas/" + formSpecialOffer.getPizza().getId();
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        // cerco le Special Offer sul DB
        Optional<SpecialOffer> specialOffer = specialOfferRepository.findById(id);
        if (specialOffer.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        model.addAttribute("specialOffer", specialOffer.get());
        return "specialOffers/form";
    }

    @PostMapping("/edit/{id}")
    public String doEdit(@PathVariable Integer id, @Valid @ModelAttribute("specialOffer") SpecialOffer formSpecialOffer, BindingResult bindingResult) {
        Optional<SpecialOffer> specialOfferToEdit = specialOfferRepository.findById(id);
        if (specialOfferToEdit.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        // setto id della special offer nel form special offer
        formSpecialOffer.setId(id);
        // salvo la special offer nel DB (update)
        specialOfferRepository.save(formSpecialOffer);
        // redirect alla pagina di dettaglio della pizza
        return "redirect:/pizzas/" + formSpecialOffer.getPizza().getId();
    }


    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        // verifico che lo special offer esiste
        Optional<SpecialOffer> specialOfferToDelete = specialOfferRepository.findById(id);
        if (specialOfferToDelete.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        // se esiste lo cancello
        specialOfferRepository.delete(specialOfferToDelete.get());
        // faccio una redirect alla pagina di dettaglio del libro
        return "redirect:/pizzas/" + specialOfferToDelete.get().getPizza().getId();
    }
}
