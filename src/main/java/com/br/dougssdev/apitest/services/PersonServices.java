package com.br.dougssdev.apitest.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import com.br.dougssdev.apitest.exceptions.ResourceNotFoundException;
import com.br.dougssdev.apitest.model.Person;
import com.br.dougssdev.apitest.repos.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class PersonServices {

    @Autowired
    private PersonRepository repository;

    public List<Person> findAll() {
        return repository.findAll();
    }

    public Person findById(Long id) {

        return  repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records for this person ID"));
    }

    public Person create(Person person) {

        Optional<Person> savedPerson = repository.findByEmail(person.getEmail());

        if(savedPerson.isPresent()){
            throw new ResourceNotFoundException("This person is already with given email: " + person.getEmail());
        }

        return repository.save(person);
    }

    public Person update(Person person) {

        Person entity = repository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records for this person ID"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());
        entity.setEmail(person.getEmail());

        return repository.save(entity);
    }

    public ResponseEntity<?> delete(Long id) {

        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records for this person ID"));

        repository.delete(entity);
        return ResponseEntity.noContent().build();
    }
}