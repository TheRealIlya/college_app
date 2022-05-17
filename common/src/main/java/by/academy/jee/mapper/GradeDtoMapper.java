package by.academy.jee.mapper;

import by.academy.jee.dto.grade.GradeDtoRequest;
import by.academy.jee.dto.grade.GradeDtoResponse;
import by.academy.jee.model.grade.Grade;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GradeDtoMapper extends GenericMapper<GradeDtoRequest, GradeDtoResponse, Grade> {
}
