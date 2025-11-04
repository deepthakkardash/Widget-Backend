package com.example.Widget.in.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserWidgetRequest {
    private Integer user_widget_id;
    private int widgetid;
    private int pos_x;
    private int pos_y;
    private int width;
    private int height;
}
