package by.academy.jee.model.auth.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class JwtResponse {

    private final String token;
}