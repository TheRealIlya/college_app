package by.academy.jee.model.person;

import by.academy.jee.model.person.role.Role;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Entity
@DiscriminatorValue("1")
public class Teacher extends Person {

    @Fetch(FetchMode.JOIN)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "salary", joinColumns = @JoinColumn(name = "teacher_id"))
    @Column(name = "value")
    private Map<Integer, Double> salaries = new HashMap<>();

    public Teacher() {
        setRole(Role.ROLE_TEACHER);
    }

    public void setSalaries(Map<Integer, Double> salaries) {
        this.salaries = salaries;
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
