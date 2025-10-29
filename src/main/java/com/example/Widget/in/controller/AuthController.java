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
        user userResponse = userService.RegisterUser(
                request.getUsername(),
                request.getPassword(),
                request.getFirstname(),
                request.getLastname()
                );

        if (userResponse != null)
        {
            ApiResponse<user> res = new ApiResponse<>(true, "Successfully Registered", userResponse);
            return ResponseEntity.ok(res);
        }

        ApiResponse<user> res = new ApiResponse<>(false, "Failed To Register", null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }



    @PostMapping("/login")
    public  ResponseEntity<ApiResponse<String>> loginUser(@RequestBody LoginRequestDto request,HttpServletResponse response)
    {
        user userResponse = userService.LoginUser(request.getUsername(), request.getPassword());

        if (userResponse != null)
        {

            String jwtToken = jwtTokenUtil.generateToken(userResponse.getUsername());

            boolean cookieSecure = false;
            ResponseCookie jwtCookie = ResponseCookie.from("Authorization", jwtToken)
                    .path("/")
                    .httpOnly(true)
                    .secure(cookieSecure)
                    .sameSite("None")
                    .maxAge(3600)
                    .build();

            response.addHeader("Set-Cookie", jwtCookie.toString());

            ApiResponse<String> res = new ApiResponse<>(true, "Successfully Logged", jwtToken);
            return ResponseEntity.ok(res);
        }

        ApiResponse<String> res = new ApiResponse<>(false, "Failed To Login", null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }
}