package by.academy.jee.web.service;

import by.academy.jee.dao.GradeDao;
import by.academy.jee.dao.GroupDao;
import by.academy.jee.dao.PersonDao;
import by.academy.jee.dao.ThemeDao;
import by.academy.jee.exception.NotFoundException;
import by.academy.jee.exception.ServiceException;
import by.academy.jee.model.grade.Grade;
import by.academy.jee.model.group.Group;
import by.academy.jee.model.person.Admin;
import by.academy.jee.model.person.Person;
import by.academy.jee.model.person.Student;
import by.academy.jee.model.theme.Theme;
import by.academy.jee.web.util.PasswordHasher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ServiceTest {

    private final PasswordHasher passwordHasher = mock(PasswordHasher.class);
    private final GroupDao groupDao = mock(GroupDao.class);
    private final ThemeDao themeDao = mock(ThemeDao.class);
    private final GradeDao gradeDao = mock(GradeDao.class);
    private final PersonDao personDao = mock(PersonDao.class);
    private final Service service = new Service(passwordHasher, groupDao, themeDao, gradeDao, personDao);

    @AfterEach
    void clearMocks() {
        clearInvocations();
    }

    @Test
    void createPersonWhenLoginIsNew() {
        Person expected = new Admin();
        expected.setLogin("New Login");
        expected.setPassword("Some password");
        when(passwordHasher.getEncryptedPassword(expected.getPassword())).thenReturn(expected.getPassword());
        when(personDao.save(expected)).thenReturn(expected);
        Person actual = service.createPerson(expected);
        assertEquals(expected, actual);
    }

    @Test
    void getPersonWhenLoginIsValid() {
        Student expected = new Student();
        Person actual = getPerson("Valid login");
        assertEquals(expected, actual);
    }

    @Test
    void getPersonWhenLoginIsNotValid() {
        assertThrows(NotFoundException.class, () -> getPerson("Wrong login"));
    }

    @Test
    void updatePersonWhenAllConditionsValid() {
        Person expected = new Admin();
        expected.setId(4);
        expected.setPassword("Some password");
        Person actual = updatePerson(expected, 4);
        assertEquals(expected, actual);
    }

    @Test
    void updatePersonWhenIdIsNotEqualToPersonId() {
        Person person = new Admin();
        person.setId(4);
        person.setPassword("Some password");
        assertThrows(ServiceException.class, () -> updatePerson(person, 5));
    }

    @Test
    void updatePersonWhenPersonIsNull() {
        assertThrows(ServiceException.class, () -> service.updatePerson(null, 4));
    }

    @Test
    void updateGradeWhenAllConditionsValid() {
        Grade expected = new Grade();
        expected.setId(2);
        Grade actual = updateGrade(expected, 2);
        assertEquals(expected, actual);
    }

    @Test
    void updateGradeWhenIdIsNotEqualToGradeId() {
        Grade grade = new Grade();
        grade.setId(3);
        assertThrows(ServiceException.class, () -> updateGrade(grade, 4));
    }

    @Test
    void updateGradeWhenGradeIsNull() {
        assertThrows(ServiceException.class, () -> service.updateGrade(null, 2));
    }

    @Test
    void removeGradeWhenInputIdIsValid() {
        removeGrade(1);
        assertTrue(true);
    }

    @Test
    void removeGradeWhenInputIsNotValid() {
        assertThrows(ServiceException.class, () -> removeGrade(2));
    }

    @Test
    void getGroupWhenInputTitleIsValid() {
        Group expected = new Group();
        expected.setTitle("Valid group title");
        Group actual = getGroup(expected.getTitle());
        assertEquals(expected, actual);
    }

    @Test
    void getGroupWhenInputTitleIsNotValid() {
        assertThrows(NotFoundException.class, () -> getGroup("Wrong group title"));
    }

    @Test
    void updateGroupWhenAllConditionsValid() {
        Group expected = new Group();
        expected.setId(2);
        Group actual = updateGroup(expected, 2);
        assertEquals(expected, actual);
    }

    @Test
    void updateGroupWhenIdIsNotEqualToGroupId() {
        Group group = new Group();
        group.setId(2);
        assertThrows(ServiceException.class, () -> updateGroup(group, 3));
    }

    @Test
    void updateGroupWhenGroupIsNull() {
        assertThrows(ServiceException.class, () -> service.updateGroup(null, 3));
    }

    @Test
    void removeGroup() {
        Grade firstGrade = new Grade();
        Grade secondGrade = new Grade();
        Group groupForTest = new Group();
        Group anotherGroup = new Group();
        Student student = new Student();
        student.setGroups(new ArrayList<>(List.of(groupForTest, anotherGroup)));
        groupForTest.setStudents(new ArrayList<>(List.of(student)));
        groupForTest.setGrades(new ArrayList<>(List.of(firstGrade, secondGrade)));
        doNothing().when(gradeDao).delete(any(Grade.class));
        when(personDao.save(student)).thenReturn(student);
        when(groupDao.save(groupForTest)).thenReturn(groupForTest);
        doNothing().when(groupDao).delete(groupForTest);
        service.removeGroup(groupForTest);
        assertNull(groupForTest.getGrades());
        assertEquals(1, student.getGroups().size());
    }

    @Test
    void getThemeWhenTitleIsValid() {
        Theme expected = new Theme();
        expected.setTitle("Valid theme title");
        Theme actual = getTheme(expected.getTitle());
        assertEquals(expected, actual);
    }

    @Test
    void getThemeWhenTitleIsNotValid() {
        assertThrows(NotFoundException.class, () -> getTheme("Wrong theme title"));
    }

    @Test
    void updateThemeWhenAllConditionsValid() {
        Theme expected = new Theme();
        expected.setId(1);
        Theme actual = updateTheme(expected, 1);
        assertEquals(expected, actual);
    }

    @Test
    void updateThemeWhenIdIsNotEqualToThemeId() {
        Theme theme = new Theme();
        theme.setId(1);
        assertThrows(ServiceException.class, () -> updateTheme(theme, 2));
    }

    @Test
    void updateThemeWhenThemeIsNull() {
        assertThrows(ServiceException.class, () -> service.updateTheme(null, 3));
    }

    @Test
    void removeTheme() {
        Grade firstGrade = new Grade();
        Grade secondGrade = new Grade();
        Theme themeForTest = new Theme();
        Theme anotherTheme = new Theme();
        Group group = new Group();
        themeForTest.setGrades(new ArrayList<>(List.of(firstGrade, secondGrade)));
        themeForTest.setGroups(new ArrayList<>(List.of(group)));
        group.setThemes(new ArrayList<>(List.of(themeForTest, anotherTheme)));
        doNothing().when(gradeDao).delete(any(Grade.class));
        when(groupDao.save(group)).thenReturn(group);
        doNothing().when(themeDao).delete(any(Theme.class));
        service.removeTheme(themeForTest);
        assertEquals(1, group.getThemes().size());
    }

    private Person getPerson(String login) {
        when(personDao.findByLogin(login)).then(invocation -> {
            if ("Valid login".equals(login)) {
                return Optional.of(new Student());
            }
            throw new NotFoundException();
        });
        return service.getPerson(login);
    }

    private Person updatePerson(Person person, int id) {
        when(passwordHasher.getEncryptedPassword(person.getPassword())).thenReturn(person.getPassword());
        when(personDao.save(person)).thenReturn(person);
        return service.updatePerson(person, id);
    }

    private Grade updateGrade(Grade grade, int id) {
        when(gradeDao.save(grade)).thenReturn(grade);
        return service.updateGrade(grade, id);
    }

    private void removeGrade(int id) {
        int validId = 1;
        when(gradeDao.findById(id)).then(invocation -> {
            if (validId == id) {
                return Optional.of(new Grade());
            }
            return Optional.empty();
        });
        doNothing().when(gradeDao).delete(any(Grade.class));
        service.removeGrade(id);
    }

    private Group getGroup(String title) {
        String validTitle = "Valid group title";
        when(groupDao.findByTitle(title)).then(invocation -> {
            if (validTitle.equals(title)) {
                Group group = new Group();
                group.setTitle(title);
                return Optional.of(group);
            }
            return Optional.empty();
        });
        return service.getGroup(title);
    }

    private Group updateGroup(Group group, int id) {
        when(groupDao.save(group)).thenReturn(group);
        return service.updateGroup(group, id);
    }

    private Theme getTheme(String title) {
        String validTitle = "Valid theme title";
        when(themeDao.findByTitle(title)).then(invocation -> {
            if (validTitle.equals(title)) {
                Theme theme = new Theme();
                theme.setTitle(title);
                return Optional.of(theme);
            }
            return Optional.empty();
        });
        return service.getTheme(title);
    }

    private Theme updateTheme(Theme theme, int id) {
        when(themeDao.save(theme)).thenReturn(theme);
        return service.updateTheme(theme, id);
    }
}