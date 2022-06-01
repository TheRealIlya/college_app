package by.academy.jee.mapper;

import by.academy.jee.dto.theme.ThemeDtoRequest;
import by.academy.jee.dto.theme.ThemeDtoResponse;
import by.academy.jee.model.theme.Theme;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ThemeDtoMapper extends GenericMapper<ThemeDtoRequest, ThemeDtoResponse, Theme> {
}