package by.academy.jee.web.service.auth;

import by.academy.jee.exception.NotFoundException;
import by.academy.jee.model.auth.UserPrincipal;
import by.academy.jee.model.person.Admin;
import by.academy.jee.web.service.Service;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest {

    private final Service service = mock(Service.class);
    private final UserService userService = new UserService(service);

    @Test
    void loadUserByUsernameWhenUsernameIsValid() {
        Admin admin = new Admin();
        admin.setLogin("Valid login");
        admin.setPassword("Some password");
        UserDetails expectedUserDetails = new UserPrincipal(admin);
        UserDetails actualUserDetails = loadUserByUsername(admin.getLogin());
        assertEquals(expectedUserDetails.getUsername(), actualUserDetails.getUsername());
        assertEquals(expectedUserDetails.getAuthorities().size(), actualUserDetails.getAuthorities().size());
        assertEquals(expectedUserDetails.getPassword(), actualUserDetails.getPassword());
    }

    @Test
    void loadUserByUsernameWhenUsernameIsNotValid() {
        assertThrows(UsernameNotFoundException.class, () -> loadUserByUsername("Wrong login"));
    }

    private UserDetails loadUserByUsername(String username) {
        when(service.getPerson(username)).then(invocation -> {
            if ("Valid login".equals(username)) {
                Admin admin = new Admin();
                admin.setLogin(username);
                admin.setPassword("Some password");
                return admin;
            }
            throw new NotFoundException();
        });
        return userService.loadUserByUsername(username);
    }
}
