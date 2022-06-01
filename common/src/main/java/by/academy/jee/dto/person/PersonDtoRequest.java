package by.academy.jee.dto.person;

import by.academy.jee.model.person.role.Role;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Getter
@Setter
public class PersonDtoRequest {

    private Integer id;
    @NotNull
    private String login;
    private String password;
    private String name;
    @Min(1)
    private int age;
    @NotNull
    private Role role;
    private Map<Integer, Double> salaries;
}