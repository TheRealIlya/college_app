package by.academy.jee.mapper;

import by.academy.jee.dto.person.PersonDtoRequest;
import by.academy.jee.dto.person.PersonDtoResponse;
import by.academy.jee.dto.person.admin.AdminDtoResponse;
import by.academy.jee.dto.person.student.StudentDtoResponse;
import by.academy.jee.dto.person.teacher.TeacherDtoResponse;
import by.academy.jee.model.person.Admin;
import by.academy.jee.model.person.Person;
import by.academy.jee.model.person.Student;
import by.academy.jee.model.person.Teacher;
import by.academy.jee.model.person.role.Role;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PersonDtoMapperTest {

    private static final AdminDtoMapper adminDtoMapper = mock(AdminDtoMapper.class);
    private static final TeacherDtoMapper teacherDtoMapper = mock(TeacherDtoMapper.class);
    private static final StudentDtoMapper studentDtoMapper = mock(StudentDtoMapper.class);
    private final PersonDtoMapper personDtoMapper = new PersonDtoMapper(adminDtoMapper, teacherDtoMapper,
            studentDtoMapper);

    @BeforeAll
    static void init() {
        AdminDtoResponse adminDtoResponse = new AdminDtoResponse();
        adminDtoResponse.setRole(Role.ROLE_ADMIN);
        TeacherDtoResponse teacherDtoResponse = new TeacherDtoResponse();
        teacherDtoResponse.setRole(Role.ROLE_TEACHER);
        StudentDtoResponse studentDtoResponse = new StudentDtoResponse();
        studentDtoResponse.setRole(Role.ROLE_STUDENT);
        when(adminDtoMapper.mapModelToDto(any(Admin.class))).thenReturn(adminDtoResponse);
        when(teacherDtoMapper.mapModelToDto(any(Teacher.class))).thenReturn(teacherDtoResponse);
        when(studentDtoMapper.mapModelToDto(any(Student.class))).thenReturn(studentDtoResponse);
        when(adminDtoMapper.mapDtoToModel(any(PersonDtoRequest.class))).thenReturn(new Admin());
        when(teacherDtoMapper.mapDtoToModel(any(PersonDtoRequest.class))).thenReturn(new Teacher());
        when(studentDtoMapper.mapDtoToModel(any(PersonDtoRequest.class))).thenReturn(new Student());
    }

    @Test
    void mapModelToDtoWhenModelIsAdmin() {
        AdminDtoResponse expected = new AdminDtoResponse();
        expected.setRole(Role.ROLE_ADMIN);
        PersonDtoResponse actual = personDtoMapper.mapModelToDto(new Admin());
        assertEquals(expected.getRole(), actual.getRole());
    }

    @Test
    void mapModelToDtoWhenModelIsTeacher() {
        TeacherDtoResponse expected = new TeacherDtoResponse();
        expected.setRole(Role.ROLE_TEACHER);
        PersonDtoResponse actual = personDtoMapper.mapModelToDto(new Teacher());
        assertEquals(expected.getRole(), actual.getRole());
    }

    @Test
    void mapModelToDtoWhenModelIsStudent() {
        StudentDtoResponse expected = new StudentDtoResponse();
        expected.setRole(Role.ROLE_STUDENT);
        PersonDtoResponse actual = personDtoMapper.mapModelToDto(new Student());
        assertEquals(expected.getRole(), actual.getRole());
    }

    @Test
    void mapModelListToDtoList() {
        Person person1 = new Admin();
        Person person2 = new Admin();
        Person person3 = new Teacher();
        Person person4 = new Student();
        Person person5 = new Student();
        Person person6 = new Student();
        List<Person> persons = new ArrayList<>(List.of(person1, person2, person3, person4, person5, person6));
        List<PersonDtoResponse> personDtoResponses = personDtoMapper.mapModelListToDtoList(persons);
        assertEquals(persons.size(), personDtoResponses.size());
    }

    @Test
    void mapDtoToModelWhenDtoIsAdmin() {
        PersonDtoRequest personDtoRequest = new PersonDtoRequest();
        personDtoRequest.setRole(Role.ROLE_ADMIN);
        Admin expected = new Admin();
        Person actual = personDtoMapper.mapDtoToModel(personDtoRequest);
        assertEquals(expected, actual);
    }

    @Test
    void mapDtoToModelWhenDtoIsTeacher() {
        PersonDtoRequest personDtoRequest = new PersonDtoRequest();
        personDtoRequest.setRole(Role.ROLE_TEACHER);
        Teacher expected = new Teacher();
        Person actual = personDtoMapper.mapDtoToModel(personDtoRequest);
        assertEquals(expected, actual);
    }

    @Test
    void mapDtoToModelWhenDtoIsStudent() {
        PersonDtoRequest personDtoRequest = new PersonDtoRequest();
        personDtoRequest.setRole(Role.ROLE_STUDENT);
        Student expected = new Student();
        Person actual = personDtoMapper.mapDtoToModel(personDtoRequest);
        assertEquals(expected, actual);
    }
}
