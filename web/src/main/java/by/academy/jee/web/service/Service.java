package by.academy.jee.web.service;

import by.academy.jee.dao.GradeDao;
import by.academy.jee.dao.GroupDao;
import by.academy.jee.dao.PersonDao;
import by.academy.jee.dao.ThemeDao;
import by.academy.jee.exception.NotFoundException;
import by.academy.jee.exception.ServiceException;
import by.academy.jee.model.grade.Grade;
import by.academy.jee.model.group.Group;
import by.academy.jee.model.person.Person;
import by.academy.jee.model.person.Student;
import by.academy.jee.model.theme.Theme;
import by.academy.jee.web.util.PasswordHasher;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@org.springframework.stereotype.Service
@Transactional
@RequiredArgsConstructor
public class Service {

    private static final String BAD_REQUEST_UPDATE_THEME = "Bad request - theme is null or id not equals to theme.id";
    private static final String BAD_REQUEST_UPDATE_GROUP = "Bad request - group is null or id not equals to group.id";
    private static final String BAD_REQUEST_UPDATE_GRADE = "Bad request - grade is null or id not equals to grade.id";
    private static final String BAD_REQUEST_UPDATE_PERSON =
            "Bad request - person is null or id not equals to person.id";
    private final PasswordHasher passwordHasher;
    private final GroupDao groupDao;
    private final ThemeDao themeDao;
    private final GradeDao gradeDao;
    private final PersonDao personDao;

    public Person createPerson(Person person) {
        encryptPasswordForPerson(person);
        return personDao.save(person);
    }

    public Person getPerson(String login) {
        return personDao.findByLogin(login).orElseThrow(NotFoundException::new);
    }

    public Person updatePerson(Person newPerson, int id) {
        if (newPerson != null && newPerson.getId() == id) {
            encryptPasswordForPerson(newPerson);
            return personDao.save(newPerson);
        }
        throw new ServiceException(BAD_REQUEST_UPDATE_PERSON);
    }

    public Person removePerson(Person person) {
        personDao.delete(person);
        return person;
    }

    public List<Person> getAllPersons() {
        return personDao.findAll();
    }

    public List<Grade> getAllGrades() {
        return gradeDao.findAll();
    }

    public Grade createGrade(Grade grade) {
        return gradeDao.save(grade);
    }

    public Grade updateGrade(Grade grade, int id) {
        if (grade != null && grade.getId() == id) {
            return gradeDao.save(grade);
        }
        throw new ServiceException(BAD_REQUEST_UPDATE_GRADE);
    }

    public Grade removeGrade(int id) {
        Grade grade = gradeDao.findById(id).orElseThrow(ServiceException::new);
        gradeDao.delete(grade);
        return grade;
    }

    public List<Group> getAllGroups() {
        return groupDao.findAll();
    }

    public Group getGroup(String title) {
        return groupDao.findByTitle(title).orElseThrow(NotFoundException::new);
    }

    public Group createGroup(Group group) {
        return groupDao.save(group);
    }

    public Group updateGroup(Group newGroup, int id) {
        if (newGroup != null && newGroup.getId() == id) {
            return groupDao.save(newGroup);
        }
        throw new ServiceException(BAD_REQUEST_UPDATE_GROUP);
    }

    public Group removeGroup(Group group) {
        for (Grade grade : group.getGrades()) {
            gradeDao.delete(grade);
        }
        group.setGrades(null);
        for (Student student : group.getStudents()) {
            student.getGroups().remove(group);
            personDao.save(student);
        }
        groupDao.save(group);
        groupDao.delete(group);
        return group;
    }

    public List<Theme> getAllThemes() {
        return themeDao.findAll();
    }

    public Theme getTheme(String title) {
        return themeDao.findByTitle(title).orElseThrow(NotFoundException::new);
    }

    public Theme createTheme(Theme theme) {
        return themeDao.save(theme);
    }

    public Theme updateTheme(Theme newTheme, int id) {
        if (newTheme != null && newTheme.getId() == id) {
            return themeDao.save(newTheme);
        }
        throw new ServiceException(BAD_REQUEST_UPDATE_THEME);
    }

    public Theme removeTheme(Theme theme) {
        theme.getGrades()
                .forEach(gradeDao::delete);
        theme.getGroups()
                .forEach(group -> {
                    group.getThemes().remove(theme);
                    groupDao.save(group);
                });
        themeDao.delete(theme);
        return theme;
    }

    private void encryptPasswordForPerson(Person person) {
        person.setPassword(passwordHasher.getEncryptedPassword(person.getPassword()));
    }
}