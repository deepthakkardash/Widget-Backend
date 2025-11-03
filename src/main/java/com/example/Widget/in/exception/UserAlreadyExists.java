package com.example.Widget.in.exception;

public class UserAlreadyExists extends RuntimeException
{
    public UserAlreadyExists(String message)
    {
        super(message);
    }
}
