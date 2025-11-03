package com.example.Widget.in.controller;

import com.example.Widget.in.config.JwtTokenUtil;
import com.example.Widget.in.dto.ApiResponse;
import com.example.Widget.in.dto.LoginRequestDto;
import com.example.Widget.in.entities.user;
import com.example.Widget.in.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController
{

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;



    @PostMapping("/register")
    public ResponseEntity<ApiResponse<user>> registerUser(@RequestBody LoginRequestDto request)
    {
        return ResponseEntity.ok(userService.RegisterUser(
                request.getUsername(),
                request.getPassword(),
                request.getFirstname(),
                request.getLastname()
        ));
    }



    @PostMapping("/login")
    public  ResponseEntity<ApiResponse<String>> loginUser(@RequestBody LoginRequestDto request,HttpServletResponse response)
    {
        return ResponseEntity.ok(userService.LoginUser(request.getUsername(), request.getPassword(),response));
    }
}