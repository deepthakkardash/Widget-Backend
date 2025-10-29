package com.example.Widget;

import com.example.Widget.in.entities.user;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WidgetApplication
{
	public static void main(String[] args)
    {
        SpringApplication.run(WidgetApplication.class, args);
		System.out.println("Hello World");
    }
}