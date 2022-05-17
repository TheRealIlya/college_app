package by.academy.jee.model.person;

import by.academy.jee.model.person.role.Role;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Setter
@Entity
@DiscriminatorValue("0")
public class Admin extends Person {

    public Admin() {
        setRole(Role.ROLE_ADMIN);
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
