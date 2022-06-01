package by.academy.jee.dao;

import by.academy.jee.model.theme.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface ThemeDao extends JpaRepository<Theme, Integer> {

    Optional<Theme> findByTitle(String title);
}