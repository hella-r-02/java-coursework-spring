package main.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.entity.UserEntity;
import main.exception.UserAlreadyExistException;
import main.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void addNewUserAfterSignUp(String username, String password, List<String> roles) {
        if (userRepository.findByUserName(username).isPresent()) {
            throw new UserAlreadyExistException("user already exist");
        }
        userRepository.save(new UserEntity(username, password, roles));
    }
}
