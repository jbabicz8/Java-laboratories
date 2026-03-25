package org.babicz.springlab4.auth;
import org.babicz.springlab4.model.Student;

public interface AuthService {
    void register(Student student);
    Tokens authenticate(Credentials credentials);
    Tokens refreshTokens(Tokens tokens);
}