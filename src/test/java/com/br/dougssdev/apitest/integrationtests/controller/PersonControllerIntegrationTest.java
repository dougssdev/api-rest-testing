package com.br.dougssdev.apitest.integrationtests.controller;

import com.br.dougssdev.apitest.config.TestConfig;
import com.br.dougssdev.apitest.integrationtests.testcontainers.AbstractIntegrationTest;
import com.br.dougssdev.apitest.model.Person;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonMappingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
class PersonControllerIntegrationTest  extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static Person person;

    @BeforeAll
    public static void setup(){

        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        specification = new RequestSpecBuilder()
                .setBasePath("/person")
                .setPort(TestConfig.SERVER_PORT)
                    .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .build();

        person = new Person("Samuel",
                "Aurélio",
                "Rio de Janeiro - Brasil",
                "male",
                "aureliosamuca05@outlook.com");
    }

    @Test
    @Order(1)
    @DisplayName("Given Person Object When Create Person Then Return Person Object")
    void testGiven_PersonObject_When_CreatePerson_Then_ReturnPersonObject() throws IOException {

      String content = given().spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .body(person)
              .when()
                .post()
              .then()
                .statusCode(200)
                    .extract()
                        .body()
                            .asString();

        Person createdPerson = objectMapper.readValue(content, Person.class);

        person = createdPerson;

        assertNotNull(createdPerson);
        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getGender());
        assertNotNull(createdPerson.getAddress());

        assertTrue(createdPerson.getId() > 0);
        assertEquals("Samuel", createdPerson.getFirstName());
        assertEquals("male", createdPerson.getGender());
    }

    @Test
    @Order(2)
    @DisplayName("Given Person Object When Update One Person Then Return Updated Person")
    void testGiven_PersonObject_When_UpdateOnePerson_Then_ReturnUpdatedPerson() throws IOException {

       person.setFirstName("Lúcio");
       person.setEmail("lucioarmelin@email.com");

        String content = given().spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .body(person)
                .when()
                    .put()
                .then()
                    .statusCode(200)
                        .extract()
                            .body()
                                .asString();

        Person value = objectMapper.readValue(content, Person.class);

        person = value;

        assertNotNull(value);
        assertTrue(value.getId() > 0);
        assertEquals("Lúcio", value.getFirstName());
        assertEquals("lucioarmelin@email.com", value.getEmail());

    }

    @Test
    @Order(3)
    @DisplayName("Given Person ID When Find By Id Then Return Person Object")
    void testGiven_PersonID_When_FindById_Then_ReturnPersonObject() throws IOException {

        String content = given().spec(specification)
                .pathParam("id", person.getId())
                .when()
                    .get("{id}")
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                        .asString();

        Person value = objectMapper.readValue(content, Person.class);

        assertNotNull(value);
        assertTrue(value.getId() > 0);
        assertEquals("Lúcio", value.getFirstName());
        assertEquals("lucioarmelin@email.com", value.getEmail());

    }

    @Test
    @Order(4)
    @DisplayName("When Find All Then Return A Persons List")
    void testWhen_FindAll_Then_ReturnAllPersonObject() throws IOException {


        Person person1 = new Person("Carlos",
                "Saíde",
                "Rio de Janeiro - Brasil",
                "male",
                "saidedeloscar12@email.com");


        given().spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .body(person1)
                .when()
                .post()
                .then()
                .statusCode(200);


        String content = given().spec(specification)
                .when()
                    .get()
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                        .asString();

        Person[] arrayPerson = objectMapper.readValue(content, Person[].class);

        List<Person> value = Arrays.asList(arrayPerson);

        assertThat(value).isNotEmpty();
        assertThat(value.get(0).getFirstName()).isEqualTo("Lúcio");
        assertThat(value.get(1).getFirstName()).isEqualTo("Carlos");

    }

    @Test
    @Order(5)
    @DisplayName("When Find All Then Return A Persons List")
    void testGiven_PersonObject_When_Delete_Then_ReturnNoContent() throws JsonMappingException {


        given().spec(specification)
                .pathParam("id", person.getId())
                .when()
                    .delete("{id}")
                .then()
                    .statusCode(204);


    }



}