package by.academy.jee.web.controller.rest;

import by.academy.jee.dto.group.GroupDtoRequest;
import by.academy.jee.dto.group.GroupDtoResponse;
import by.academy.jee.mapper.GroupDtoMapper;
import by.academy.jee.model.group.Group;
import by.academy.jee.web.service.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/rest/groups")
public class GroupJsonController {

    private final Service service;
    private final GroupDtoMapper groupDtoMapper;

    @GetMapping
    public List<GroupDtoResponse> getAllGroups() {
        return groupDtoMapper.mapModelListToDtoList(service.getAllGroups());
    }

    @GetMapping(value = "/{title}")
    public ResponseEntity<GroupDtoResponse> getGroup(@PathVariable @NotNull String title) {
        return ResponseEntity.ok(groupDtoMapper.mapModelToDto(service.getGroup(title)));
    }

    @PostMapping
    public ResponseEntity<GroupDtoResponse> createGroup(@Valid @RequestBody GroupDtoRequest groupDtoRequest) {
        Group group = groupDtoMapper.mapDtoToModel(groupDtoRequest);
        return ResponseEntity.ok(groupDtoMapper.mapModelToDto(service.createGroup(group)));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<GroupDtoResponse> updateGroup(@Valid @RequestBody GroupDtoRequest groupDtoRequest,
                                                        @PathVariable @Min(1) int id) {
        Group group = groupDtoMapper.mapDtoToModel(groupDtoRequest);
        return ResponseEntity.ok(groupDtoMapper.mapModelToDto(service.updateGroup(group, id)));
    }

    @DeleteMapping(value = "/{title}")
    public ResponseEntity<GroupDtoResponse> deleteGroup(@PathVariable @NotNull String title) {
        Group group = service.getGroup(title);
        return ResponseEntity.ok(groupDtoMapper.mapModelToDto(service.removeGroup(group)));
    }
}