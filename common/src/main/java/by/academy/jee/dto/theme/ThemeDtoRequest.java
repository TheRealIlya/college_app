package by.academy.jee.dto.theme;

import by.academy.jee.dto.grade.GradeDtoRequest;
import by.academy.jee.dto.group.GroupDtoRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class ThemeDtoRequest {

    private Integer id;
    @NotNull
    private String title;
    private List<GroupDtoRequest> groups;
    private List<GradeDtoRequest> grades;
}