package by.academy.jee.web.controller.rest;

import by.academy.jee.dto.grade.GradeDtoRequest;
import by.academy.jee.dto.grade.GradeDtoResponse;
import by.academy.jee.mapper.GradeDtoMapper;
import by.academy.jee.model.grade.Grade;
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
import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/rest/grades")
public class GradeJsonController {

    private final Service service;
    private final GradeDtoMapper gradeDtoMapper;

    @GetMapping
    public List<GradeDtoResponse> getAllGrades() {
        return gradeDtoMapper.mapModelListToDtoList(service.getAllGrades());
    }

    @PostMapping
    public ResponseEntity<GradeDtoResponse> createGrade(@Valid @RequestBody GradeDtoRequest gradeDtoRequest) {
        Grade grade = gradeDtoMapper.mapDtoToModel(gradeDtoRequest);
        return ResponseEntity.ok(gradeDtoMapper.mapModelToDto(service.createGrade(grade)));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<GradeDtoResponse> updateGrade(@Valid @RequestBody GradeDtoRequest gradeDtoRequest,
                                                        @PathVariable @Min(1) int id) {
        Grade grade = gradeDtoMapper.mapDtoToModel(gradeDtoRequest);
        return ResponseEntity.ok(gradeDtoMapper.mapModelToDto(service.updateGrade(grade, id)));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<GradeDtoResponse> deleteGrade(@PathVariable @Min(1) int id) {
        return ResponseEntity.ok(gradeDtoMapper.mapModelToDto(service.removeGrade(id)));
    }
}