package by.academy.jee.mapper;

import by.academy.jee.dto.group.GroupDtoRequest;
import by.academy.jee.dto.group.GroupDtoResponse;
import by.academy.jee.model.group.Group;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GroupDtoMapper extends GenericMapper<GroupDtoRequest, GroupDtoResponse, Group> {
}
