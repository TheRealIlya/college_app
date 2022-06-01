package by.academy.jee.mapper;

import by.academy.jee.dto.person.PersonDtoRequest;
import by.academy.jee.dto.person.admin.AdminDtoResponse;
import by.academy.jee.model.person.Admin;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdminDtoMapper extends GenericMapper<PersonDtoRequest, AdminDtoResponse, Admin> {
}