package com.example.Widget.in.exception;

public class WidgetNotFoundException extends RuntimeException
{
    public WidgetNotFoundException(String message)
    {
        super(message);
    }
}
