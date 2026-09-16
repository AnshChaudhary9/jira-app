package com.testApp.jira.services;

import com.testApp.jira.entities.User;
import com.testApp.jira.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServicesImpl implements UserDetailsService {

    @Autowired
    public UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String fullname) throws UsernameNotFoundException {
        User user = userRepository.findByFullName(fullname);
        if (user!=null){
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getFullName())
                    .password(user.getPassword())
                    .roles(user.getRole().name())
                    .build();
        }
        throw new UsernameNotFoundException("Username not found with name: " + fullname);
    }

}
