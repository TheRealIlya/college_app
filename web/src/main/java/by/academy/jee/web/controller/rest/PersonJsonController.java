package by.academy.jee.web.controller.rest;

import by.academy.jee.dto.person.PersonDtoRequest;
import by.academy.jee.dto.person.PersonDtoResponse;
import by.academy.jee.mapper.PersonDtoMapper;
import by.academy.jee.model.person.Person;
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
@RequiredArgsConstructor
@Validated
@RequestMapping(value = "/rest/persons")
public class PersonJsonController {

    private final Service service;
    private final PersonDtoMapper personDtoMapper;

    @GetMapping
    public List<PersonDtoResponse> getAllPersons() {
        return personDtoMapper.mapModelListToDtoList(service.getAllPersons());
    }

    @GetMapping(value = "/{login}")
    public ResponseEntity<PersonDtoResponse> getPerson(@PathVariable @NotNull String login) {
        Person person = service.getPerson(login);
        return ResponseEntity.ok(personDtoMapper.mapModelToDto(person));
    }

    @PostMapping
    public ResponseEntity<PersonDtoResponse> createPerson(@Valid @RequestBody PersonDtoRequest personDtoRequest) {
        Person person = personDtoMapper.mapDtoToModel(personDtoRequest);
        return ResponseEntity.ok(personDtoMapper.mapModelToDto(service.createPerson(person)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonDtoResponse> updatePerson(@Valid @RequestBody PersonDtoRequest personDtoRequest,
                                                          @PathVariable @Min(1) int id) {
        Person person = personDtoMapper.mapDtoToModel(personDtoRequest);
        return ResponseEntity.ok(personDtoMapper.mapModelToDto(service.updatePerson(person, id)));
    }

    @DeleteMapping(value = "/{login}")
    public ResponseEntity<PersonDtoResponse> deletePerson(@PathVariable @NotNull String login) {
        Person person = service.getPerson(login);
        return ResponseEntity.ok(personDtoMapper.mapModelToDto(service.removePerson(person)));
    }
}