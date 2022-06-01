package by.academy.jee.web.controller.rest;

import by.academy.jee.dto.grade.GradeDtoRequest;
import by.academy.jee.exception.ServiceException;
import by.academy.jee.mapper.GradeDtoMapper;
import by.academy.jee.model.grade.Grade;
import by.academy.jee.web.handler.ControllerExceptionHandler;
import by.academy.jee.web.service.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GradeJsonControllerTest {

    private static MockMvc mockMvc;
    private static final Service service = mock(Service.class);
    private static final GradeDtoMapper gradeDtoMapper = Mappers.getMapper(GradeDtoMapper.class);

    @BeforeAll
    static void initMockMvc() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new GradeJsonController(service, gradeDtoMapper))
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @AfterEach
    void clearAllMocks() {
        clearInvocations();
    }

    @Test
    void getAllGradesTest() throws Exception {
        Grade grade1 = new Grade();
        grade1.setId(1);
        Grade grade2 = new Grade();
        grade2.setId(2);
        Grade grade3 = new Grade();
        grade3.setId(3);
        List<Grade> grades = new ArrayList<>(List.of(grade1, grade2, grade3));
        when(service.getAllGrades()).thenReturn(grades);
        mockMvc.perform(get("/rest/grades")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[2].id").value(3));
    }

    @Test
    void createGradeTest() throws Exception {
        GradeDtoRequest gradeDtoRequest = new GradeDtoRequest();
        gradeDtoRequest.setValue(5);
        Grade grade = gradeDtoMapper.mapDtoToModel(gradeDtoRequest);
        when(service.createGrade(grade)).thenReturn(grade);
        mockMvc.perform(post("/rest/grades")
                        .content(new ObjectMapper().writeValueAsString(gradeDtoRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value").value(5));
    }

    @Test
    void updateGradeWhenIdIsNotEqualToIdInPath() throws Exception {
        GradeDtoRequest gradeDtoRequest = new GradeDtoRequest();
        gradeDtoRequest.setId(3);
        gradeDtoRequest.setValue(5);
        Grade grade = gradeDtoMapper.mapDtoToModel(gradeDtoRequest);
        when(service.updateGrade(grade, 2)).thenThrow(ServiceException.class);
        mockMvc.perform(put("/rest/grades/2")
                        .content(new ObjectMapper().writeValueAsString(gradeDtoRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateGradeWhenIdIsEqualToIdInPath() throws Exception {
        GradeDtoRequest gradeDtoRequest = new GradeDtoRequest();
        gradeDtoRequest.setId(3);
        gradeDtoRequest.setValue(5);
        Grade grade = gradeDtoMapper.mapDtoToModel(gradeDtoRequest);
        when(service.updateGrade(grade, 3)).thenReturn(grade);
        mockMvc.perform(put("/rest/grades/3")
                        .content(new ObjectMapper().writeValueAsString(gradeDtoRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.value").value(5));
    }

    @Test
    void deleteGradeWhenIdIsNotExist() throws Exception {
        int wrongId = 4;
        when(service.removeGrade(wrongId)).thenThrow(ServiceException.class);
        mockMvc.perform(delete("/rest/grades/4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteGradeWhenIdIsExist() throws Exception {
        int legalId = 2;
        Grade grade = new Grade();
        grade.setId(legalId);
        grade.setValue(4);
        when(service.removeGrade(2)).thenReturn(grade);
        mockMvc.perform(delete("/rest/grades/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.value").value(4));
    }
}
