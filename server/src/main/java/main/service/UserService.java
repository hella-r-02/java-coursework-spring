package main.service;

import java.util.List;

public interface UserService {
    void addNewUserAfterSignUp(String username, String password, List<String> roles);
}
