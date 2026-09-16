package com.testApp.jira.repositories;

import com.testApp.jira.entities.Role;
import com.testApp.jira.entities.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User , ObjectId> {

    User findByFullName(String fullName);
    User findUserById(ObjectId Id);
    List<User> findByRole(Role role);
    User deleteByFullName(String fullName);
}
