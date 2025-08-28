package com.br.dougssdev.apitest.repos;

import com.br.dougssdev.apitest.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {}
