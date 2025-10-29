package com.example.Widget.in.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto
{
    private String username;
    private String password;
    private String firstname;
    private String lastname;
}
