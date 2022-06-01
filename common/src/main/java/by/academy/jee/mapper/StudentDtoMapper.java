package by.academy.jee.mapper;

import by.academy.jee.dto.person.PersonDtoRequest;
import by.academy.jee.dto.person.student.StudentDtoResponse;
import by.academy.jee.model.person.Student;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentDtoMapper extends GenericMapper<PersonDtoRequest, StudentDtoResponse, Student> {
}