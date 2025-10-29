package com.example.Widget.in.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Entity
@Table(name = "widgets")
public class widget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int widgetid;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private String iconPath;

    @Column
    private int defaultHeight;

    @Column
    private int defaultWidth;

    public widget(String title, String description, int defaultHeight, int defaultWidth, String iconPath)
    {
        this.title = title;
        this.description = description;
        this.iconPath = iconPath;
        this.defaultHeight = defaultHeight;
        this.defaultWidth = defaultWidth;
    }
}