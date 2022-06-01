package by.academy.jee.web.controller.rest;

import by.academy.jee.dto.group.GroupDtoRequest;
import by.academy.jee.exception.NotFoundException;
import by.academy.jee.exception.ServiceException;
import by.academy.jee.mapper.GroupDtoMapper;
import by.academy.jee.model.group.Group;
import by.academy.jee.web.handler.ControllerExceptionHandler;
import by.academy.jee.web.service.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GroupJsonControllerTest {

    private static MockMvc mockMvc;
    private static final Service service = mock(Service.class);
    private static final GroupDtoMapper groupDtoMapper = Mappers.getMapper(GroupDtoMapper.class);

    @BeforeAll
    static void initMockMvc() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new GroupJsonController(service, groupDtoMapper))
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @AfterEach
    void clearAllMocks() {
        clearInvocations();
    }

    @Test
    void getAllGroupsTest() throws Exception {
        Group group1 = new Group();
        group1.setId(1);
        group1.setTitle("group1");
        Group group2 = new Group();
        group2.setId(2);
        group2.setTitle("group2");
        List<Group> groups = new ArrayList<>(List.of(group1, group2));
        when(service.getAllGroups()).thenReturn(groups);
        mockMvc.perform(get("/rest/groups")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("group1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("group2"));
    }

    @Test
    void getGroupWhenTitleIsNotValid() throws Exception {
        when(service.getGroup("InvalidTitle")).thenThrow(NotFoundException.class);
        mockMvc.perform(get("/rest/groups/InvalidTitle"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getGroupWhenTitleIsValid() throws Exception {
        Group group = new Group();
        group.setId(1);
        group.setTitle("ValidTitle");
        when(service.getGroup(group.getTitle())).thenReturn(group);
        mockMvc.perform(get("/rest/groups/ValidTitle")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value(group.getTitle()));
    }

    @Test
    void createGroupWhenTitleIsAlreadyExist() throws Exception {
        GroupDtoRequest groupDtoRequest = new GroupDtoRequest();
        groupDtoRequest.setId(2);
        groupDtoRequest.setTitle("InvalidTitle");
        Group group = groupDtoMapper.mapDtoToModel(groupDtoRequest);
        when(service.createGroup(group)).thenThrow(DuplicateKeyException.class);
        mockMvc.perform(post("/rest/groups")
                        .content(new ObjectMapper().writeValueAsString(groupDtoRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void createGroupWhenTitleIsValid() throws Exception {
        GroupDtoRequest groupDtoRequest = new GroupDtoRequest();
        groupDtoRequest.setId(2);
        groupDtoRequest.setTitle("ValidTitle");
        Group group = groupDtoMapper.mapDtoToModel(groupDtoRequest);
        when(service.createGroup(group)).thenReturn(group);
        mockMvc.perform(post("/rest/groups")
                        .content(new ObjectMapper().writeValueAsString(groupDtoRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.title").value(group.getTitle()));
    }

    @Test
    void updateGroupWhenIdIsNotEqualToIdInPath() throws Exception {
        GroupDtoRequest groupDtoRequest = new GroupDtoRequest();
        groupDtoRequest.setId(2);
        groupDtoRequest.setTitle("Some title");
        Group group = groupDtoMapper.mapDtoToModel(groupDtoRequest);
        when(service.updateGroup(group, 3)).thenThrow(ServiceException.class);
        mockMvc.perform(put("/rest/groups/3")
                        .content(new ObjectMapper().writeValueAsString(groupDtoRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateGroupWhenIdIsEqualToIdInPath() throws Exception {
        GroupDtoRequest groupDtoRequest = new GroupDtoRequest();
        groupDtoRequest.setId(2);
        groupDtoRequest.setTitle("Some title");
        Group group = groupDtoMapper.mapDtoToModel(groupDtoRequest);
        when(service.updateGroup(group, 2)).thenReturn(group);
        mockMvc.perform(put("/rest/groups/2")
                        .content(new ObjectMapper().writeValueAsString(groupDtoRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2));
    }
}