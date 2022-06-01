package by.academy.jee.dao;

import by.academy.jee.model.person.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface PersonDao extends JpaRepository<Person, Integer> {

    Optional<Person> findByLogin(String login);
}