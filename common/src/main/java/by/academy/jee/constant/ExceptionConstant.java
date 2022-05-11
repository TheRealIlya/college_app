package by.academy.jee.constant;

public class ExceptionConstant {

    private ExceptionConstant() {
        //util class
    }

    public static final String GRADE_UPDATE_ERROR = "Error - Grade id must be equal with id in path";
    public static final String GROUP_UPDATE_ERROR = "Error - Group id must be equal with id in path";
    public static final String PERSON_UPDATE_ERROR = "Error - Person's id must be equal with id in path";
    public static final String THEME_UPDATE_ERROR = "Error - Theme id must be equal with id in path";
    public static final String USER_IS_ALREADY_EXIST = "Error - user with this login is already exist";
    public static final String ERROR_GROUP_ALREADY_EXIST = "Error - this group is already exist";
    public static final String ERROR_THEME_ALREADY_EXIST = "Error - this theme is already exist";
    public static final String NO_SUCH_ENTITY_WITH_THIS_ID = "No such entity with this id";
    public static final String ERROR_NO_TEACHERS_IN_DATABASE = "Error - no teachers in database";
    public static final String ERROR_NO_STUDENTS_IN_DATABASE = "Error - no students in database";
}
