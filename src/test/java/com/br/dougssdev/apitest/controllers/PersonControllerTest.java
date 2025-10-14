package com.br.dougssdev.apitest.controllers;

import com.br.dougssdev.apitest.exceptions.ResourceNotFoundException;
import com.br.dougssdev.apitest.model.Person;
import com.br.dougssdev.apitest.services.PersonServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PersonServices personServices;

    Person person;


    @BeforeEach
    void setup(){
        person = new Person("Samuel",
                "Aurélio",
                "Rio de Janeiro - Brasil",
                "male",
                "samuelaurelio20@gmail.com");
    }

    @Test
    @DisplayName("Given Person Object When CreatePerson Should Return SavedPerson")
    void testGivenPersonObject_WhenCreatePerson_ShouldReturnSavedPerson() throws Exception {
        //Given
        given(personServices.create(any(Person.class))).willAnswer((invocation) -> {
            return invocation.getArgument(0);
        });

        //When

        ResultActions response = mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person)));

        //Then

        response.andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(person.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(person.getLastName())))
                .andExpect(jsonPath("$.email", is(person.getEmail())));

        verify(personServices).create(any(Person.class));
    }

    @Test
    @DisplayName("Given Person List When FindAll Should Return PersonList")
    void testGivenPersonList_WhenFindAll_ShouldReturnPersonList() throws Exception {
        //Given

        Person p = new Person("Luciano",
                "Aurélio",
                "Rio de Janeiro - Brasil",
                "male",
                "lucianoaurelio20@gmail.com");

        List<Person> people = List.of(person, p);


        given(personServices.findAll()).willReturn(people);

        //When

        ResultActions response = mockMvc.perform(get("/person"));

        //Then
        response.
                andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(people.size())));
    }

    @Test
    @DisplayName("Given PersonId When FindById Should Return Person Object")
    void GivenPersonId_WhenFindById_ShouldReturnPersonObject() throws Exception {
        //Given
            long personId = 1l;
            given(personServices.findById(personId)).willReturn(person);

        //When

        ResultActions response = mockMvc.perform(get("/person/{id}", personId));

        //Then
        response.
                andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(person.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(person.getLastName())))
                .andExpect(jsonPath("$.email", is(person.getEmail())));
    }

    @Test
    @DisplayName("Given PersonId When FindById Shoul Return Not Found")
    void GivenPersonId_WhenFindById_ShouldReturn_NotFound() throws Exception {
        //Given
        long personId = 1l;
        given(personServices.findById(personId)).willThrow(ResourceNotFoundException.class);

        //When

        ResultActions response = mockMvc.perform(get("/person/{id}", personId));

        //Then
        response.
                andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("Given Person Object When UpdatePerson Should Return UpdatedPerson")
    void testGivenPersonObject_WhenUpdatePerson_ShouldReturnUpdatedPerson() throws Exception {
        //Given

        Long personId = 1l;
        given(personServices.findById(personId)).willReturn(person);

        given(personServices.update(any(Person.class))).willAnswer(
                (invocation) -> invocation.getArgument(0));
        //When

        Person updatedPerson = new Person("Simão",
                "Aurélio",
                "Rio de Janeiro - Brasil",
                "male",
                "simaolaurelio20@gmail.com");

        ResultActions response = mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedPerson)));

        //Then

        response.andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(updatedPerson.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedPerson.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedPerson.getEmail())));
    }

    @Test
    @DisplayName("Given PersonId  When Delete Should Return No Content")
    void testGivenPersonID_WhenDelete_ShouldReturnNotContent() throws Exception {
        //Given

        Long personId = 1l;
        willDoNothing().given(personServices).delete(personId);

        //When

        ResultActions response = mockMvc.perform(delete("/person/{id}", personId));

        //Then

        response.andExpect(status().isNoContent())
                .andDo(print());
    }

}