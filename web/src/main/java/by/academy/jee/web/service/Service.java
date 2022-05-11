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
import by.academy.jee.web.aspect.service.ServiceExceptionHandler;
import by.academy.jee.web.util.PasswordHasher;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static by.academy.jee.constant.CommonConstant.REPOSITORY_PROPERTIES;
import static by.academy.jee.constant.ExceptionConstant.ERROR_GROUP_ALREADY_EXIST;
import static by.academy.jee.constant.ExceptionConstant.ERROR_THEME_ALREADY_EXIST;
import static by.academy.jee.constant.ExceptionConstant.USER_IS_ALREADY_EXIST;
import static by.academy.jee.constant.ServiceConstant.ADMIN_PREFIX;
import static by.academy.jee.constant.ServiceConstant.GRADE_PREFIX;
import static by.academy.jee.constant.ServiceConstant.GROUP_PREFIX;
import static by.academy.jee.constant.ServiceConstant.NO_SUCH_USER_IN_DATABASE;
import static by.academy.jee.constant.ServiceConstant.STUDENT_PREFIX;
import static by.academy.jee.constant.ServiceConstant.TEACHER_PREFIX;
import static by.academy.jee.constant.ServiceConstant.THEME_PREFIX;

@Slf4j
@org.springframework.stereotype.Service
@Transactional
@PropertySource(REPOSITORY_PROPERTIES)
@Setter
public class Service {

    private final String type;
    private final PasswordHasher passwordHasher;

    private PersonDao<Admin> adminDao;
    private PersonDao<Teacher> teacherDao;
    private PersonDao<Student> studentDao;
    @Autowired
    private Map<String, PersonDao> personDaoMap;

    private GroupDao groupDao;
    @Autowired
    private Map<String, GroupDao> groupDaoMap;

    private ThemeDao themeDao;
    @Autowired
    private Map<String, ThemeDao> themeDaoMap;

    private GradeDao gradeDao;
    @Autowired
    private Map<String, GradeDao> gradeDaoMap;

    public Service(@Value("${repository.type}") String type, PasswordHasher passwordHasher) {
        this.type = StringUtils.capitalize(type);
        this.passwordHasher = passwordHasher;
    }

    @PostConstruct
    private void init() {
        String adminDaoTitle = ADMIN_PREFIX + type;
        String teacherDaoTitle = TEACHER_PREFIX + type;
        String studentDaoTitle = STUDENT_PREFIX + type;
        String groupDaoTitle = GROUP_PREFIX + type;
        String themeDaoTitle = THEME_PREFIX + type;
        String gradeDaoTitle = GRADE_PREFIX + type;
        adminDao = personDaoMap.get(adminDaoTitle);
        teacherDao = personDaoMap.get(teacherDaoTitle);
        studentDao = personDaoMap.get(studentDaoTitle);
        groupDao = groupDaoMap.get(groupDaoTitle);
        themeDao = themeDaoMap.get(themeDaoTitle);
        gradeDao = gradeDaoMap.get(gradeDaoTitle);
    }

    public Person getPersonFromDto(PersonDto personDto) {
        Map<Role, Person> personMap = Map.of(Role.ROLE_ADMIN, new Admin(),
                Role.ROLE_TEACHER, new Teacher().withSalaries(personDto.getSalaries()),
                Role.ROLE_STUDENT, new Student());
        Person person = personMap.get(personDto.getRole());
        if (personDto.getId() != null) {
            person.setId(personDto.getId());
        }
        person.setLogin(personDto.getLogin());
        person.setPassword(getEncryptedPassword(personDto.getPassword()));
        person.setName(personDto.getName());
        person.setAge(personDto.getAge());
        return person;
    }

    @ServiceExceptionHandler
    public Person createPerson(Person person) throws ServiceException {
        checkIsUserNotExist(person.getLogin());
        switch (person.getRole()) {
            case ROLE_ADMIN:
                return adminDao.create((Admin) person);
            case ROLE_TEACHER:
                return teacherDao.create((Teacher) person);
            case ROLE_STUDENT:
            default:
                return studentDao.create((Student) person);
        }
    }

    public Person getUserIfExist(String login) throws ServiceException {
        Person user;
        try {
            user = studentDao.read(login);
        } catch (DaoException e) {
            try {
                user = teacherDao.read(login);
            } catch (DaoException f) {
                try {
                    user = adminDao.read(login);
                } catch (DaoException g) {
                    log.error("Error - no user {} in database", login);
                    throw new ServiceException(NO_SUCH_USER_IN_DATABASE);
                }
            }
        }
        return user;
    }

    public List<Person> getAllPersons() {
        List<Person> persons = new ArrayList<>(adminDao.readAll());
        List<Teacher> teachers = teacherDao.readAll();
        List<Student> students = studentDao.readAll();
        persons.addAll(teachers);
        persons.addAll(students);
        return persons;
    }

    public List<Grade> getAllGrades() {
        return gradeDao.readAll();
    }

    @ServiceExceptionHandler
    public Grade createGrade(Grade grade) throws ServiceException {
        return gradeDao.create(grade);
    }

    @ServiceExceptionHandler
    public Grade updateGrade(Grade grade) throws ServiceException {
        return gradeDao.update(grade);
    }

    @ServiceExceptionHandler
    public Grade removeGrade(int id) throws ServiceException {
        Grade grade = gradeDao.read(id);
        gradeDao.delete(grade);
        return grade;
    }

    public List<Group> getAllGroups() {
        return groupDao.readAll();
    }

    @ServiceExceptionHandler
    public Group getGroup(String title) throws ServiceException {
        return groupDao.read(title);
    }

    public Group createGroup(Group group) throws ServiceException {
        checkIsGroupNotExist(group.getTitle());
        return groupDao.create(group);
    }

    @ServiceExceptionHandler
    public Group updateGroup(Group newGroup) throws ServiceException {
        groupDao.update(newGroup);
        return newGroup;
    }

    @ServiceExceptionHandler
    public Group removeGroup(Group group) throws ServiceException {
        for (Grade grade : group.getGrades()) {
            gradeDao.delete(grade);
        }
        group.setGrades(null);
        for (Student student : group.getStudents()) {
            student.getGroups().remove(group);
            studentDao.update(student);
        }
        groupDao.update(group);
        groupDao.delete(group.getTitle());
        return group;
    }

    public List<Theme> getAllThemes() {
        return themeDao.readAll();
    }

    @ServiceExceptionHandler
    public Theme getTheme(String title) throws ServiceException {
        return themeDao.read(title);
    }

    public Theme createTheme(Theme theme) throws ServiceException {
        checkIsThemeNotExist(theme.getTitle());
        return themeDao.create(theme);
    }

    @ServiceExceptionHandler
    public Theme updateTheme(Theme newTheme) throws ServiceException {
        themeDao.update(newTheme);
        return newTheme;
    }

    @ServiceExceptionHandler
    public Theme removeTheme(Theme theme) throws ServiceException {
        theme.getGrades()
                .forEach(grade -> gradeDao.delete(grade));
        theme.getGroups()
                .forEach(group -> {
                    group.getThemes().remove(theme);
                    groupDao.update(group);
                });
        themeDao.delete(theme.getTitle());
        return theme;
    }

    @ServiceExceptionHandler
    public Person updatePerson(Person newPerson) throws ServiceException {
        switch (newPerson.getRole()) {
            case ROLE_ADMIN:
                adminDao.update((Admin) newPerson);
                break;
            case ROLE_TEACHER:
                teacherDao.update((Teacher) newPerson);
                break;
            case ROLE_STUDENT:
                studentDao.update((Student) newPerson);
        }
        return newPerson;
    }

    @ServiceExceptionHandler
    public Person removeUser(Person person) throws ServiceException {
        switch (person.getRole()) {
            case ROLE_ADMIN:
                adminDao.delete(person.getLogin());
                break;
            case ROLE_TEACHER:
                try {
                    Group group = groupDao.read((Teacher) person);
                    group.setTeacher(null);
                    groupDao.update(group);
                } catch (DaoException e) {
                    //ignore
                }
                teacherDao.delete(person.getLogin());
                break;
            case ROLE_STUDENT:
            default:
                Student student = (Student) person;
                student.getGrades()
                        .forEach(grade -> gradeDao.delete(grade));
                studentDao.delete(person.getLogin());
        }
        return person;
    }

    private String getEncryptedPassword(String password) {
        return passwordHasher.getEncryptedPassword(password);
    }

    private void checkIsGroupNotExist(String title) throws ServiceException {
        try {
            groupDao.read(title);
        } catch (DaoException e) {
            return;
        }
        log.error("Error - attempt to add already existed group {}", title);
        throw new ServiceException(ERROR_GROUP_ALREADY_EXIST);
    }

    private void checkIsThemeNotExist(String title) throws ServiceException {
        try {
            themeDao.read(title);
        } catch (DaoException e) {
            return;
        }
        log.error("Error - attempt to add already existed theme {}", title);
        throw new ServiceException(ERROR_THEME_ALREADY_EXIST);
    }

    private void checkIsUserNotExist(String login) throws ServiceException {
        try {
            adminDao.read(login);
        } catch (DaoException e) {
            try {
                teacherDao.read(login);
            } catch (DaoException f) {
                try {
                    studentDao.read(login);
                } catch (DaoException g) {
                    return;
                }
            }
        }
        log.error("Error - attempt to add already existed user {}", login);
        throw new ServiceException(USER_IS_ALREADY_EXIST);
    }
}
