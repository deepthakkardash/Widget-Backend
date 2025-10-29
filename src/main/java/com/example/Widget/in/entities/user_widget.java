package com.example.Widget.in.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "user_widget")
@Data
//@NoArgsConstructor
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class user_widget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int user_widget_id;

    @ManyToOne
    @JoinColumn(name = "userid",nullable = false)
    private user user;

    @ManyToOne
    @JoinColumn(name = "widgetid",nullable = false)
    private widget widget;

    @Column
    private int pos_x;

    @Column
    private int pos_y;

    @Column
    private int width;

    @Column
    private int height;
}
