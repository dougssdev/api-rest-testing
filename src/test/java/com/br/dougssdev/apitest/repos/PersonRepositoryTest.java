package com.br.dougssdev.apitest.repos;

import com.br.dougssdev.apitest.model.Person;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository repository;

    private Person person0;

    @BeforeEach
    public void setup(){

       person0 = new Person("Samuel",
                "Aurélio",
                "Rio de Janeiro - Brasil",
                "male",
                "aureliosamuca05@outlook.com");
    }

    @Test
    @DisplayName("test Given Person when Save then Return Saved Person")
    void testGivenPerson_whenSave_thenReturnSavedPerson(){

        //When

        Person savedPerson = repository.save(person0);

        //Then

        assertNotNull(savedPerson);
        assertTrue(savedPerson.getId() > 0);

    }

    @Test
    @DisplayName("Test Given Person List when Find All then Return Person List")
    void testGivenPersonList_whenFindAll_thenReturnPersonList(){

        //Given
        Person person1 = new Person("Maria",
                "Samoa",
                "São Paulo - Brasil",
                "female",
                "mariasamoa16@outlook.com");


         repository.save(person0);
         repository.save(person1);

        //When

        List<Person> personList = repository.findAll();

        //Then

        assertNotNull(personList);
        assertEquals(2, personList.size());
    }

    @Test
    @DisplayName("Test Given Person Object when Find By Id then Return Person")
    void testGivenPersonObject_whenFindById_thenReturnPerson(){

         repository.save(person0);

        //When

        Optional<Person> savedPerson = repository.findById(person0.getId());

        //Then

        assertNotNull(savedPerson);
        assertTrue(savedPerson.get().getId() > 0);
        assertEquals(person0.getId(), savedPerson.get().getId() );
    }

    @Test
    @DisplayName("Test Given Person Object when Find By Email then Return Person")
    void testGivenPersonObject_whenFindByEmail_thenReturnPerson(){

        repository.save(person0);

        //When

        Optional<Person> savedPerson = repository.findByEmail(person0.getEmail());

        //Then

        assertNotNull(savedPerson);
        assertEquals(person0.getEmail(),
                savedPerson.get().getEmail());
    }

    @Test
    @DisplayName("Test Given Person Object when Find By Email then Return Person Updated")
    void testGivenPersonObject_whenFindByEmail_thenReturnPersonUpdated(){

        repository.save(person0);

        //When

        Person savedPerson = repository.findByEmail(person0.getEmail()).get();
        savedPerson.setFirstName("Samael");
        savedPerson.setEmail("samaelio21@outlook.com");

        Person updatedPerson = repository.save(savedPerson);

        //Then
        assertNotNull(updatedPerson);
        assertEquals("Samael", updatedPerson.getFirstName());
        assertEquals("samaelio21@outlook.com", updatedPerson.getEmail());

    }

    @Test
    @DisplayName("Test given Person object when delete then remove Person ")
    void testGivenPersonObject_whenDelete_thenRemovePerson(){

        //Given


        Person savedPerson = repository.save(person0);

        //When

        repository.deleteById(savedPerson.getId());

        Optional<Person> personById = repository.findById(savedPerson.getId());

        //Then
        assertTrue(personById.isEmpty());

    }

    @Test
    @DisplayName("test Given First Name And Last Name when Find By Full Name then Return Person")
    void testGivenFirstNameAndLastName_whenFindByFullName_thenReturnPerson(){

        //Given

        repository.save(person0);

        //When

        Person savedPerson = repository.findByFirstAndLastName(person0.getFirstName(), person0.getLastName());

        //Then

        assertNotNull(savedPerson);
        assertEquals("Samuel", savedPerson.getFirstName());
        assertEquals("Aurélio", savedPerson.getLastName());
    }

    @Test
    @DisplayName("test Given Person Object when Find By Address then Return Person List")
    void testGivenPersonObject_whenFindByAddress_thenReturnPersonList(){

        //Given

        Person person1 = new Person("Lúcio",
                "Aurélio",
                "Rio de Janeiro - Brasil",
                "male",
                "lucioaur@outlook.com");

        Person person2 = new Person("Milane",
                "Aggretti",
                "Zagrebe - Croácia",
                "female",
                "aggmil4@gmail.com");

        repository.save(person0);
        repository.save(person1);
        repository.save(person2);

        //When

        List<Person> personList = repository.findPersonByAddress("Rio de Janeiro - Brasil");

        //Then

        assertNotNull(personList);
        assertEquals(2, personList.size());
        assertEquals("Rio de Janeiro - Brasil", personList.get(0).getAddress());
        assertEquals("Rio de Janeiro - Brasil", personList.get(1).getAddress());
    }

    @Test
    @DisplayName("test Given Person Object when Find By First name and email then Return Person Object")
    void testGivenPersonObject_whenFindByEmailAndFirstName_thenReturnPerson(){

        //Given

        repository.save(person0);


        //When

        Person person = repository.findByFirstNameAndEmail(person0.getFirstName(), person0.getEmail());

        //Then

        assertNotNull(person);
        assertEquals("Rio de Janeiro - Brasil", person.getAddress());
        assertEquals("male", person.getGender());

    }

    @Test
    @DisplayName("test Given Person Object when Find By First name and email with named parameters then Return Person Object")
    void testGivenPersonObject_whenFindByEmailAndFirstNameWithNamedParameters_thenReturnPerson(){

        //Given

        repository.save(person0);


        //When

        Person person = repository.findByFirstNameAndEmailWithNamedParameters(person0.getFirstName(), person0.getEmail());

        //Then

        assertNotNull(person);
        assertEquals("Rio de Janeiro - Brasil", person.getAddress());
        assertEquals("male", person.getGender());

    }

}
