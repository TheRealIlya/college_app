package by.academy.jee.dto.person;

import by.academy.jee.model.person.role.Role;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonDtoResponse {

    private Integer id;
    private String login;
    private String name;
    @JsonBackReference
    private Role role;
}