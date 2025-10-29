package com.example.Widget.in.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WidgetResponse
{
    private int id;
    private String title;
    private String description;
    private int defaultHeight;
    private int defaultWidth;
}
