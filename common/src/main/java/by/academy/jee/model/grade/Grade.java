package by.academy.jee.model.grade;

import by.academy.jee.model.AbstractEntity;
import by.academy.jee.model.group.Group;
import by.academy.jee.model.person.Student;
import by.academy.jee.model.theme.Theme;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@NoArgsConstructor
@Entity
public class Grade extends AbstractEntity {

    private int value;

    @Fetch(FetchMode.JOIN)
    @ManyToOne
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "student_id")
    private Student student;

    @Fetch(FetchMode.JOIN)
    @ManyToOne
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "group_id")
    private Group group;

    @Fetch(FetchMode.JOIN)
    @ManyToOne
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "theme_id")
    private Theme theme;

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
