package com.example.Widget.in.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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


    @Column(name = "created_by_user_id", updatable = false)
    private Integer createdByUserId;

    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "edited_by_user_id")
    private Integer editedByUserId;

    @Column(name = "edited_date")
    private LocalDateTime editedDate;

    @PrePersist
    public void onCreate() {
        this.createdDate = LocalDateTime.now();
//        this.createdByUserId = getCurrentUserId();
    }

    @PreUpdate
    public void onUpdate() {
        this.editedDate = LocalDateTime.now();
//        this.editedByUserId = getCurrentUserId();
    }


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
