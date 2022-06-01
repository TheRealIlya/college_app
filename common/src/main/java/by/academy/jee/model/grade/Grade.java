package by.academy.jee.model.grade;

import by.academy.jee.model.AbstractEntity;
import by.academy.jee.model.group.Group;
import by.academy.jee.model.person.Student;
import by.academy.jee.model.theme.Theme;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Grade extends AbstractEntity {

    private int value;

    @Fetch(FetchMode.JOIN)
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @Fetch(FetchMode.JOIN)
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Fetch(FetchMode.JOIN)
    @ManyToOne
    @JoinColumn(name = "theme_id")
    private Theme theme;

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}