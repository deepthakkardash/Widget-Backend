package com.example.Widget.in.service;

import com.example.Widget.in.entities.user;
import com.example.Widget.in.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService
{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public user RegisterUser(String username, String password, String firstname, String lastname)
    {
        user user= userRepository.findByUsername(username);
        if(user==null)
        {
            String encodedPassword = passwordEncoder.encode(password);
            user newUser = new user(username, encodedPassword, firstname, lastname);
            user savedUser = userRepository.save(newUser);
            savedUser.setFullnameAfterLoad();
            return savedUser;
        }
        return null;
    }



    public  user LoginUser(String username, String password)
    {
        user existinguser= userRepository.findByUsername(username);
        if (existinguser != null && passwordEncoder.matches(password, existinguser.getPassword())) {
            existinguser.setFullnameAfterLoad();
            return existinguser;
        }
        return null;
    }
}
