package com.testApp.jira.services;

import com.testApp.jira.entities.Role;
import com.testApp.jira.entities.User;
import com.testApp.jira.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    public static final PasswordEncoder passwordencoder = new BCryptPasswordEncoder();

    @Autowired
    private UserRepository userRepository;

    public void saveNewDev(User user){
            user.setPassword(passwordencoder.encode(user.getPassword()));
            user.setRole(Role.DEVELOPER);
            userRepository.save(user);
    }

    public void saveNewTester(User user){
        user.setPassword(passwordencoder.encode(user.getPassword()));
        user.setRole(Role.TESTER);
        userRepository.save(user);
    }

    public void saveAdmin(User user){
        user.setPassword(passwordencoder.encode(user.getPassword()));
        user.setRole(Role.ADMIN);
        userRepository.save(user);
    }

    public void saveManager(User user){
        user.setPassword(passwordencoder.encode(user.getPassword()));
        user.setRole(Role.MANAGER);
        userRepository.save(user);
    }

    public void saveUser(User user){
        userRepository.save(user);
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public User findById(ObjectId id){
        return userRepository.findUserById(id);
    }

    public void deleteById(ObjectId id){
        userRepository.deleteById(id);
    }

    public User findByFullName(String fullName){
        return userRepository.findByFullName(fullName);
    }

    public List<User> findByUserRole(Role role){
        return userRepository.findByRole(role);
    }
}
