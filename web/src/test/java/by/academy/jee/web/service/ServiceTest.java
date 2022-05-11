package by.academy.jee.web.service;

import by.academy.jee.dao.grade.GradeDao;
import by.academy.jee.dao.group.GroupDao;
import by.academy.jee.dao.person.PersonDao;
import by.academy.jee.dao.theme.ThemeDao;
import by.academy.jee.exception.DaoException;
import by.academy.jee.exception.ServiceException;
import by.academy.jee.model.grade.Grade;
import by.academy.jee.model.group.Group;
import by.academy.jee.model.person.Admin;
import by.academy.jee.model.person.Person;
import by.academy.jee.model.person.PersonDto;
import by.academy.jee.model.person.Student;
import by.academy.jee.model.person.Teacher;
import by.academy.jee.model.person.role.Role;
import by.academy.jee.model.theme.Theme;
import by.academy.jee.web.util.PasswordHasher;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ServiceTest {

    private final PasswordHasher hasher = mock(PasswordHasher.class);

    @Test
    void get_person_from_dto_when_input_is_correct() {
        String login = "Login";
        String password = "qwe";
        String name = "Ilya";
        String ageString = "25";
        PersonDto personDto = PersonDto.builder()
                .login(login)
                .password(password)
                .name(name)
                .age(Integer.parseInt(ageString))
                .role(Role.ROLE_ADMIN)
                .build();
        Person expected = new Admin()
                .withLogin(login)
                .withPassword(password)
                .withName(name)
                .withAge(Integer.parseInt(ageString));
        Service service = new Service("anyString", hasher);
        Person actual = service.getPersonFromDto(personDto);
        assertEquals(expected, actual);
    }

    @Test
    void get_admin_with_legal_login_expect_admin() throws Exception {
        String login = "adminLogin";
        Person expected = new Admin().withLogin(login);
        Person actual = getUserIfExistTest(login);
        assertEquals(expected, actual);
    }

    @Test
    void get_teacher_with_legal_login_expect_teacher() throws Exception {
        String login = "teacherLogin";
        Person expected = new Teacher().withLogin(login);
        Person actual = getUserIfExistTest(login);
        assertEquals(expected, actual);
    }

    @Test
    void get_student_with_legal_login_expect_student() throws Exception {
        String login = "studentLogin";
        Person expected = new Student().withLogin(login);
        Person actual = getUserIfExistTest(login);
        assertEquals(expected, actual);
    }

    @Test
    void get_student_with_illegal_login_expect_service_exception() {
        String login = "wrongLogin";
        assertThrows(ServiceException.class, () -> getUserIfExistTest(login));
    }

    @Test
    void get_all_persons_when_total_amount_is_7_expect_collection_of_7_persons() {
        List<Admin> admins = List.of(new Admin(), new Admin(), new Admin());
        List<Teacher> teachers = List.of(new Teacher(), new Teacher());
        List<Student> students = List.of(new Student(), new Student());
        PersonDao<Admin> adminDao = mock(PersonDao.class);
        PersonDao<Teacher> teacherDao = mock(PersonDao.class);
        PersonDao<Student> studentDao = mock(PersonDao.class);
        when(adminDao.readAll()).thenReturn(admins);
        when(teacherDao.readAll()).thenReturn(teachers);
        when(studentDao.readAll()).thenReturn(students);
        Service service = new Service("anyString", hasher);
        service.setAdminDao(adminDao);
        service.setTeacherDao(teacherDao);
        service.setStudentDao(studentDao);
        int expected = 7;
        int actual = service.getAllPersons().size();
        assertEquals(expected, actual);
    }

    @Test
    void remove_group_test_expect_group_without_grades_and_students_without_this_group() throws ServiceException {
        Group group = new Group();
        Student student1 = new Student();
        student1.setGroups(new ArrayList<>(List.of(group, new Group())));
        Student student2 = new Student();
        student2.setGroups(new ArrayList<>(List.of(group)));
        group.setStudents(new ArrayList<>(List.of(student1, student2)));
        group.setGrades(new ArrayList<>(List.of(new Grade(), new Grade(), new Grade())));
        GradeDao gradeDao = mock(GradeDao.class);
        PersonDao<Student> studentDao = mock(PersonDao.class);
        GroupDao groupDao = mock(GroupDao.class);
        Service service = new Service("anyString", hasher);
        service.setGradeDao(gradeDao);
        service.setStudentDao(studentDao);
        service.setGroupDao(groupDao);
        service.removeGroup(group);
        assertNull(group.getGrades());
        assertEquals(1, student1.getGroups().size());
        assertEquals(0, student2.getGroups().size());
    }

    @Test
    void remove_theme_test_expect_groups_without_removed_theme() throws ServiceException {
        Theme theme = new Theme();
        theme.setTitle("themeForRemove");
        theme.setGrades(new ArrayList<>(List.of(new Grade(), new Grade())));
        Group group1 = new Group();
        Group group2 = new Group();
        group1.setThemes(new ArrayList<>(List.of(theme, new Theme(), new Theme())));
        group2.setThemes(new ArrayList<>(List.of(theme)));
        theme.setGroups(new ArrayList<>(List.of(group1, group2)));
        ThemeDao themeDao = mock(ThemeDao.class);
        GroupDao groupDao = mock(GroupDao.class);
        GradeDao gradeDao = mock(GradeDao.class);
        Service service = new Service("anyString", hasher);
        service.setThemeDao(themeDao);
        service.setGroupDao(groupDao);
        service.setGradeDao(gradeDao);
        service.removeTheme(theme);
        assertEquals(2, group1.getThemes().size());
        assertEquals(0, group2.getThemes().size());
    }

    private Person getUserIfExistTest(String login) throws Exception {
        PersonDao<Student> studentDao = mock(PersonDao.class);
        PersonDao<Teacher> teacherDao = mock(PersonDao.class);
        PersonDao<Admin> adminDao = mock(PersonDao.class);
        when(studentDao.read(login)).then(invocation -> {
            if ("studentLogin".equals(login)) {
                return new Student().withLogin(login);
            }
            throw new DaoException("not a student");
        });
        when(teacherDao.read(login)).then(invocation -> {
            if ("teacherLogin".equals(login)) {
                return new Teacher().withLogin(login);
            }
            throw new DaoException("not a teacher");
        });
        when(adminDao.read(login)).then(invocation -> {
            if ("adminLogin".equals(login)) {
                return new Admin().withLogin(login);
            }
            throw new DaoException("not an admin");
        });
        Service service = new Service("anyString", hasher);
        service.setStudentDao(studentDao);
        service.setTeacherDao(teacherDao);
        service.setAdminDao(adminDao);
        return service.getUserIfExist(login);
    }
}
