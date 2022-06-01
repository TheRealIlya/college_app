package by.academy.jee.dto.person.teacher;

import by.academy.jee.dto.person.PersonDtoResponse;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class TeacherDtoResponse extends PersonDtoResponse {

    @EqualsAndHashCode.Exclude
    private Map<Integer, Double> salaries;
}