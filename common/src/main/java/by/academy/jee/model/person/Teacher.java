package by.academy.jee.model.person;

import by.academy.jee.model.person.role.Role;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;

@Data
@Entity
@Table(name = "users")
@SecondaryTable(name = "roles", pkJoinColumns = {@PrimaryKeyJoinColumn(name = "id")})
public class Teacher extends Person {

    @Fetch(FetchMode.JOIN)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "salary", joinColumns = @JoinColumn(name = "teacher_id"))
    @Column(name = "value")
    private Map<Integer, Double> salaries = new HashMap<>();

    public Teacher() {
        setRole(Role.ROLE_TEACHER);
    }

    public Teacher withLogin(String login) {
        setLogin(login);
        return this;
    }

    public Teacher withSalaries(Map<Integer, Double> salaries) {
        setSalaries(salaries);
        return this;
    }

    public void setSalaries(Map<Integer, Double> salaries) {
        this.salaries = salaries;
    }

    @Override
    public String toString() {
        String mapString = "";
        for (int i = 1; i < 13; i++) {
            mapString += "<br>" + i + " - " + String.format("%.2f", salaries.get(i)).replace(',', '.');
        }
        return super.toString() + "<br><br> Salaries:" + mapString;
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
