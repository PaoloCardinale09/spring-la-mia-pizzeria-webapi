package org.lessons.springpizzeria.repository;

import org.lessons.springpizzeria.model.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PizzaRepository extends JpaRepository<Pizza, Integer> {
    // metodo per ricercare la pizza con nome passato
    List<Pizza> findByName(String name);

    List<Pizza> findByNameContainingIgnoreCase(String name);

    //metodo per cercare una cosa univoca
}
