package by.academy.jee.model.auth;

import by.academy.jee.model.person.Person;
import by.academy.jee.model.person.Teacher;
import by.academy.jee.model.person.role.Role;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserPrincipalTest {

    @Test
    void userPrincipalTest() {
        Person person = new Teacher();
        UserPrincipal userPrincipal = new UserPrincipal(person);
        SimpleGrantedAuthority expected = new SimpleGrantedAuthority(Role.ROLE_TEACHER.toString());
        Collection<? extends GrantedAuthority> actualAuthorities = userPrincipal.getAuthorities();
        assertEquals(1, actualAuthorities.size());
        assertEquals(expected, actualAuthorities.iterator().next());
    }
}
