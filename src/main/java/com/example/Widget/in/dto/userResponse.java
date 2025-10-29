package com.example.Widget.in.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class userResponse
{
    private int userid;
    private String username;
    private String password;
    private String fullname;
}
