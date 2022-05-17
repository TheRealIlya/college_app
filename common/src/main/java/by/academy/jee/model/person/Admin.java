package by.academy.jee.model.person;

import by.academy.jee.model.person.role.Role;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "users")
@SecondaryTable(name = "roles", pkJoinColumns = {@PrimaryKeyJoinColumn(name = "id")})
public class Admin extends Person {

    public Admin() {
        setRole(Role.ROLE_ADMIN);
    }

    public Admin withLogin(String login) {
        setLogin(login);
        return this;
    }

    public Admin withPassword(String password) {
        setPassword(password);
        return this;
    }

    public Admin withName(String name) {
        setName(name);
        return this;
    }

    public Admin withAge(int age) {
        setAge(age);
        return this;
    }

    @Override
    public String toString() {
        return super.toString();
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
