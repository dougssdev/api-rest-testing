package com.br.dougssdev.apitest.repos;

import com.br.dougssdev.apitest.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByEmail(String email);

    @Query("""
            SELECT p FROM Person p
            WHERE p.firstName =?1
            AND p.lastName =?2
            """)
    Person findByFirstAndLastName(String firstName, String lastName);

    @Query("""
            SELECT p FROM Person p
            WHERE p.address =:address
            """)
    List<Person> findPersonByAddress(@Param("address") String address);

    @Query(value = """
            SELECT * FROM person p 
            WHERE p.first_name =?1 
            AND p.email =?2
            """,
            nativeQuery = true)
    Person findByFirstNameAndEmail(String firstName, String email);

    @Query(value = """
            SELECT * FROM person p 
            WHERE p.first_name =:firstName 
            AND p.email =:email
            """,
            nativeQuery = true)
    Person findByFirstNameAndEmailWithNamedParameters(@Param("firstName") String firstName,
                                                      @Param("email") String email);

}


