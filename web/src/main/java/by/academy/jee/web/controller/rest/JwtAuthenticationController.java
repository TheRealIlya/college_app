package by.academy.jee.web.controller.rest;

import by.academy.jee.model.auth.jwt.JwtRequest;
import by.academy.jee.model.auth.jwt.JwtResponse;
import by.academy.jee.web.service.auth.UserService;
import by.academy.jee.web.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class JwtAuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    @PostMapping(value = "/rest/authenticate")
    public ResponseEntity<JwtResponse> createAuthenticationToken(@RequestBody JwtRequest jwtRequest) {
        authenticate(jwtRequest.getLogin(), jwtRequest.getPassword());
        final UserDetails userDetails = userService.loadUserByUsername(jwtRequest.getLogin());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}