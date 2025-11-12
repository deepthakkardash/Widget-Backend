package com.example.Widget.in.service;

import com.example.Widget.in.config.JwtTokenUtil;
import com.example.Widget.in.dto.ApiResponse;
import com.example.Widget.in.entities.user;
import com.example.Widget.in.exception.UserNotFoundException;
import com.example.Widget.in.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService
{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    public ApiResponse<user> RegisterUser(String username, String password, String firstname, String lastname)
    {
        user user= userRepository.findByUsername(username);
        if(user==null)
        {
            String encodedPassword = passwordEncoder.encode(password);
            user newUser = new user(username, encodedPassword, firstname, lastname);
            user savedUser = userRepository.save(newUser);
            savedUser.setFullnameAfterLoad();
            return new ApiResponse<>(true,"Register Successfully",savedUser);
        }
        throw new UserNotFoundException("Username Already Exists");
    }


    public  ApiResponse<String> LoginUser(String username, String password, HttpServletResponse response)
    {
        user existinguser= userRepository.findByUsername(username);
        if (existinguser != null && passwordEncoder.matches(password, existinguser.getPassword())) {
            existinguser.setFullnameAfterLoad();

            String jwtToken = jwtTokenUtil.generateToken(existinguser.getUsername());

            boolean cookieSecure = false;
            ResponseCookie jwtCookie = ResponseCookie.from("Authorization", jwtToken)
                    .path("/")
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("None")
                    .maxAge(24 * 60 * 60)
                    .build();

            response.addHeader("Set-Cookie", jwtCookie.toString());

            return new ApiResponse<>(true, "Successfully Logged", jwtToken);


        }
        throw new UserNotFoundException("Invalid Credentials");
    }
}
