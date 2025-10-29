package com.example.Widget.in.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class user {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userid;

    @Column
    private String username;

    @Column
    private String password;

    @JsonIgnore
    @Column
    private String firstname;

    @JsonIgnore
    @Column
    private String lastname;

    @Transient
    private String fullname;

    @PostLoad
    @PostPersist
    public void setFullnameAfterLoad() {
        this.fullname = firstname + " " + lastname;
    }


    public user(String username, String password, String firstname, String lastname)
    {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
    }
}
