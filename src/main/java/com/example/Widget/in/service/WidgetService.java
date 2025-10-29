package com.example.Widget.in.service;


import com.example.Widget.in.dto.ApiResponse;
import com.example.Widget.in.entities.widget;
import com.example.Widget.in.repository.WidgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WidgetService {

    @Autowired
    private WidgetRepository widgetRepository;



    public widget findWidgetById(int id) {
        return widgetRepository.findById(id);
    }


    public boolean AddWidget(String title, String description, String iconPath, int defaultHeight, int defaultWidth) {
        widget widget = new widget(title, description, defaultHeight, defaultWidth, iconPath);
        widgetRepository.save(widget);
        return true;
    }

    public widget UpdateWidget(String title, String description, String iconPath, int defaultHeight, int defaultWidth) {
        widget widget = new widget(title, description, defaultHeight, defaultWidth, iconPath);
        return widgetRepository.save(widget);
    }


    public boolean DeleteWidget(int id) {
        widget widget = widgetRepository.findById(id);
        if (widget != null) {
            widgetRepository.delete(widget);
            return true;
        }
        return false;
    }

    public widget findWidgetByTitle(String title) {
        widget widget = findWidgetByTitle(title);
        if (widget != null) {
            return widget;
        }
        return null;
    }

    public ApiResponse<List<widget>> listWidgets() {
        return new ApiResponse<>(true,"Successfully Fetched",widgetRepository.findAll());

    }
}
