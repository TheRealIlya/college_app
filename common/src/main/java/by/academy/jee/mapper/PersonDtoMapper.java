package by.academy.jee.mapper;

import by.academy.jee.dto.person.PersonDtoRequest;
import by.academy.jee.dto.person.PersonDtoResponse;
import by.academy.jee.model.person.Admin;
import by.academy.jee.model.person.Person;
import by.academy.jee.model.person.Student;
import by.academy.jee.model.person.Teacher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PersonDtoMapper implements GenericMapper<PersonDtoRequest, PersonDtoResponse, Person> {

    private final AdminDtoMapper adminDtoMapper;
    private final TeacherDtoMapper teacherDtoMapper;
    private final StudentDtoMapper studentDtoMapper;

    @Override
    public PersonDtoResponse mapModelToDto(Person model) {
        switch (model.getRole()) {
            case ROLE_ADMIN:
                return adminDtoMapper.mapModelToDto((Admin) model);
            case ROLE_STUDENT:
                return studentDtoMapper.mapModelToDto((Student) model);
            case ROLE_TEACHER:
            default:
                return teacherDtoMapper.mapModelToDto((Teacher) model);
        }
    }

    @Override
    public List<PersonDtoResponse> mapModelListToDtoList(List<Person> models) {
        List<PersonDtoResponse> personDtoResponseList = new ArrayList<>();
        for (Person model : models) {
            switch (model.getRole()) {
                case ROLE_ADMIN:
                    personDtoResponseList.add(adminDtoMapper.mapModelToDto((Admin) model));
                    break;
                case ROLE_TEACHER:
                    personDtoResponseList.add(teacherDtoMapper.mapModelToDto((Teacher) model));
                    break;
                case ROLE_STUDENT:
                default:
                    personDtoResponseList.add(studentDtoMapper.mapModelToDto((Student) model));
            }
        }
        return personDtoResponseList;
    }

    @Override
    public Person mapDtoToModel(PersonDtoRequest dtoRequest) {
        switch (dtoRequest.getRole()) {
            case ROLE_ADMIN:
                return adminDtoMapper.mapDtoToModel(dtoRequest);
            case ROLE_TEACHER:
                return teacherDtoMapper.mapDtoToModel(dtoRequest);
            case ROLE_STUDENT:
            default:
                return studentDtoMapper.mapDtoToModel(dtoRequest);
        }
    }
}