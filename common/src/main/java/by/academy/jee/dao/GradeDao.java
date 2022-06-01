package by.academy.jee.dao;

import by.academy.jee.model.grade.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface GradeDao extends JpaRepository<Grade, Integer> {
}