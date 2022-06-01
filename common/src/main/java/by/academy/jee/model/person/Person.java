package by.academy.jee.model.person;

import by.academy.jee.model.AbstractEntity;
import by.academy.jee.model.person.role.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role_id", discriminatorType = DiscriminatorType.INTEGER)
@SecondaryTable(name = "roles", pkJoinColumns = {@PrimaryKeyJoinColumn(name = "id")})
public abstract class Person extends AbstractEntity {

    private String login;
    private String password;
    private String name;
    private int age;
    @Column(name = "role_id", insertable = false, updatable = false)
    private Role role;

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}