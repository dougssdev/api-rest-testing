package com.br.dougssdev.apitest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.br.dougssdev.apitest.model.Person;
import com.br.dougssdev.apitest.services.PersonServices;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonServices service;
    ///private PersonServices service = new PersonServices();

    @GetMapping
    public List<Person> findAll() {
        return service.findAll();
    }

    @GetMapping (value = "/{id}")
    public ResponseEntity<Person> findById(@PathVariable(value = "id") Long id) {

        try {
            return ResponseEntity.ok(service.findById(id));
        } catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Person create(@RequestBody Person person) {
        return service.create(person);
    }

    @PutMapping
    public ResponseEntity<Person> update(@RequestBody Person person) {
        try {
            return ResponseEntity.ok(service.update(person));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

    }


    @DeleteMapping (value = "/{id}")
    public ResponseEntity<Person> delete(@PathVariable(value = "id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}