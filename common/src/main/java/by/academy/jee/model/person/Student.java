package by.academy.jee.model.person;

import by.academy.jee.model.grade.Grade;
import by.academy.jee.model.group.Group;
import by.academy.jee.model.person.role.Role;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.List;

@Getter
@Setter
@Entity
@DiscriminatorValue("2")
public class Student extends Person {
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "student", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH})
    private List<Grade> grades;

    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany
    @JoinTable(
            name = "group_student",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private List<Group> groups;

    public Student() {
        setRole(Role.ROLE_STUDENT);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}