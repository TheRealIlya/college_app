package by.academy.jee.mapper;

import by.academy.jee.dto.person.PersonDtoRequest;
import by.academy.jee.dto.person.teacher.TeacherDtoResponse;
import by.academy.jee.model.person.Teacher;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeacherDtoMapper extends GenericMapper<PersonDtoRequest, TeacherDtoResponse, Teacher> {
}
