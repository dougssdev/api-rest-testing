package com.br.dougssdev.apitest.services;

import com.br.dougssdev.apitest.exceptions.ResourceNotFoundException;
import com.br.dougssdev.apitest.model.Person;
import com.br.dougssdev.apitest.repos.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PersonServicesTest {

    @Mock
    private PersonRepository repository;

    @InjectMocks
    private PersonServices services;

    Person person0;

    @BeforeEach
    public void setup(){

        person0 = new Person("Samuel",
                "Aurélio",
                "Rio de Janeiro - Brasil",
                "male",
                "samuelaurelio20@gmail.com");
    }


    @Test
    @DisplayName("Given Person Object When Create Then Return Saved Person")
    void testGivenPersonObject_When_Create_ThenReturnSavedPerson(){

        //given

        given(repository.findByEmail(anyString())).willReturn(Optional.empty());
        given(repository.save(person0)).willReturn(person0);

        //When

        Person savedPerson = services.create(person0);

        //Then

        assertNotNull(savedPerson);
        assertEquals(person0.getEmail(), savedPerson.getEmail());

    }

    @Test
    @DisplayName("Given Existing Email When Create Then Throws Exception")
    void testGivenExistingEmail_When_Create_ThenThrowsException(){

        //given

        given(repository.findByEmail(anyString())).willReturn(Optional.of(person0));

        //When

        assertThrows(ResourceNotFoundException.class, () -> {
            Person savedPerson = services.create(person0);
        });
        //Then

        verify(repository, never()).save(any(Person.class));

    }

    @Test
    @DisplayName("Given People List When Find All People Then Return People List")
    void testGivenPeopleList_When_FindAllPeople_ThenReturnPeopleList(){

        //given

        Person person2 = new Person("Milane",
                "Aggretti",
                "Zagrebe - Croácia",
                "female",
                "aggmil4@gmail.com");

        given(repository.findAll()).willReturn(List.of(person0, person2));

        //When
        List<Person> peopleList = services.findAll();
        //Then

        assertNotNull(peopleList);
        assertEquals(2, peopleList.size());

    }

    @Test
    @DisplayName("Given Empty People List When Find All People Then Return Empty People List")
    void testGivenEmptyPeopleList_When_FindAllPeople_ThenReturnEmptyPeopleList(){

        //given

        given(repository.findAll()).willReturn(Collections.emptyList());

        //When
        List<Person> peopleList = services.findAll();
        //Then

        assertTrue(peopleList.isEmpty());
        assertEquals(0, peopleList.size());

    }

    @Test
    @DisplayName("Given PersonId When Find By Id Then Return Person Object")
    void testGivenPersonId_When_FindById_ThenReturnPersonObject(){

        //given

        given(repository.findById(anyLong())).willReturn(Optional.of(person0));

        //When
        Person optionalPerson = services.findById(1l);
        //Then

        assertNotNull(optionalPerson);
        assertEquals("Samuel", person0.getFirstName());
        assertEquals(person0.getId(), optionalPerson.getId());

    }

    @Test
    @DisplayName("Given Person Object When Update Person Then Return Updated Person")
    void testGivenPersonObject_When_UpdatePerson_ThenReturnUpdatedPerson(){

        //given

        person0.setId(1l);
        given(repository.findById(anyLong())).willReturn(Optional.of(person0));

        person0.setEmail("hl3@gmail.com");
        person0.setFirstName("Gordon");

        given(repository.save(person0)).willReturn(person0);

        //When
        Person updatedPerson = services.update(person0);
        //Then

        assertNotNull(updatedPerson);
        assertEquals("Gordon", person0.getFirstName());
        assertEquals("hl3@gmail.com", person0.getEmail());

    }@Test
    @DisplayName("Given PersonID When Delete Person Then Do Nothing")
    void testGivenPersonID_When_DeletePerson_ThenDoNothing(){

        //given
        person0.setId(1l);
        given(repository.findById(anyLong())).willReturn(Optional.of(person0));
        willDoNothing().given(repository).delete(person0);

        //When
        services.delete(person0.getId());
        //Then

        verify(repository, times(1)).delete(person0);

    }

}