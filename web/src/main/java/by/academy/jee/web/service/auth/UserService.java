package by.academy.jee.web.service.auth;

import by.academy.jee.exception.NotFoundException;
import by.academy.jee.model.auth.UserPrincipal;
import by.academy.jee.model.person.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final by.academy.jee.web.service.Service service;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Person person = service.getPerson(username);
            return new UserPrincipal(person);
        } catch (NotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }
}