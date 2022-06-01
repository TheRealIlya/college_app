package by.academy.jee.dao;

import by.academy.jee.model.group.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface GroupDao extends JpaRepository<Group, Integer> {

    Optional<Group> findByTitle(String title);
}