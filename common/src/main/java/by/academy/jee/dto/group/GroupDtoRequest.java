package by.academy.jee.dto.group;

import by.academy.jee.dto.grade.GradeDtoRequest;
import by.academy.jee.dto.person.PersonDtoRequest;
import by.academy.jee.dto.theme.ThemeDtoRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
public class GroupDtoRequest {

    private Integer id;
    @NotNull
    private String title;
    private PersonDtoRequest teacher;
    private List<PersonDtoRequest> students;
    private List<ThemeDtoRequest> themes;
    private List<GradeDtoRequest> grades;
}