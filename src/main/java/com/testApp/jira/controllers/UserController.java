package com.testApp.jira.controllers;

import com.testApp.jira.entities.Entry;
import com.testApp.jira.entities.EntryType;
import com.testApp.jira.entities.User;
import com.testApp.jira.services.EntryService;
import com.testApp.jira.services.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EntryService entryService;

    @GetMapping("/all-users")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> getAllUsers(){
        List<User> all = userService.getAll();
        if (all != null && !all.isEmpty()){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/new-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createAdmin(@RequestBody User user){
        try {
            userService.saveAdmin(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/new-manager")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createManager(@RequestBody User user){
        try {
            userService.saveManager(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/new-dev")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createDev(@RequestBody User user){
        try {
            userService.saveNewDev(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/new-tester")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createTester(@RequestBody User user){
        try {
            userService.saveNewTester(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update-user/id/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@RequestBody User user,@PathVariable ObjectId id){
            User userInDb = userService.findById(id);
            if (userInDb == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            userInDb.setFullName(user.getFullName());
            userInDb.setPassword(user.getPassword());
            userService.saveUser(userInDb);
            return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete-user/id/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable ObjectId id){
        try {
            userService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
