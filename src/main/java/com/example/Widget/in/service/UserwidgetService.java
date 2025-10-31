package com.example.Widget.in.service;

import com.example.Widget.in.dto.ApiResponse;
import com.example.Widget.in.dto.UserWidgetRequest;
import com.example.Widget.in.dto.UserWidgetResponse;
import com.example.Widget.in.exception.UserNotFoundException;
import com.example.Widget.in.exception.UserWidgetNotFoundException;
import com.example.Widget.in.exception.WidgetNotFoundException;
import com.example.Widget.in.repository.UserRepository;
import com.example.Widget.in.repository.UserWidgetRepository;
import com.example.Widget.in.repository.WidgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.Widget.in.entities.user_widget;
import com.example.Widget.in.entities.user;
import com.example.Widget.in.entities.widget;
import org.springframework.transaction.annotation.Transactional;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UserwidgetService
{

    @Autowired
    private UserWidgetRepository userWidgetRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WidgetRepository widgetRepository;


    public ApiResponse<List<UserWidgetResponse>> getusersAllWidget(int userid)
    {
              List<UserWidgetResponse> reslist=  userWidgetRepository.findByUser_Userid(userid)
                .stream()
                .map(uw-> UserWidgetResponse.builder()

                        .widgetid(uw.getUser_widget_id())
                        .userid(uw.getUser().getUserid())
                        .widgetid(uw.getWidget().getWidgetid())
                        .pos_x(uw.getPos_x())
                        .pos_y(uw.getPos_y())
                        .width(uw.getWidth())
                        .height(uw.getHeight())
                        .build())
                .collect(Collectors.toList());

        return new ApiResponse<>(true,"Successfully fetched", reslist);
    }

    public ApiResponse<String> AddUserWidget(UserWidgetRequest[] userwidgets)
    {
        List<UserWidgetRequest> requestList = Arrays.asList(userwidgets);

        // Check for missing user/widget manually
        for (UserWidgetRequest req : requestList) {
            Optional<user> userEntity = userRepository.findById(req.getUserid());
            if (userEntity.isEmpty()) {
               throw  new UserNotFoundException("User not found with id: " + req.getUserid());
            }

            Optional<widget> widgetEntity = Optional.ofNullable(widgetRepository.findById(req.getWidgetid()));
            if (widgetEntity.isEmpty()) {
                throw new WidgetNotFoundException("Widget not found with id: " + req.getWidgetid());
            }
        }

        // If all exist, map and save
        List<user_widget> entities = requestList.stream().map(req -> {
            user userEntity = userRepository.findById(req.getUserid()).get();
            widget widgetEntity = widgetRepository.findById(req.getWidgetid());

            return user_widget.builder()
                    .user(userEntity)
                    .widget(widgetEntity)
                    .pos_x(req.getPos_x())
                    .pos_y(req.getPos_y())
                    .width(req.getWidth())
                    .height(req.getHeight())
                    .build();
        }).collect(Collectors.toList());

        userWidgetRepository.saveAll(entities);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Widgets added successfully!")
                .data("Total added: " + entities.size())
                .build();
    }

    @Transactional
    public ApiResponse<String> DeleteUserWidget(int user_widget_id)
    {
        if(userWidgetRepository.existsById(user_widget_id))
        {
            userWidgetRepository.deleteById(user_widget_id);
            return new ApiResponse<>(true,"Successfully deleted",null);
        }
        throw new WidgetNotFoundException("Widget not found with id: " + user_widget_id);
    }



    public ApiResponse<String> UpdateUserWidget(UserWidgetRequest[] userwidgets)
    {
        List<UserWidgetRequest> requestList = Arrays.asList(userwidgets);

        for (UserWidgetRequest req : requestList) {
            // Check if user_widget exists
            Optional<user_widget> existingUW = userWidgetRepository.findById(req.getUser_widget_id());
            if (existingUW.isEmpty()) {
                throw new UserWidgetNotFoundException("UserWidget not found with id: " + req.getUser_widget_id());
            }

            // Validate user
            Optional<user> userEntity = userRepository.findById(req.getUserid());
            if (userEntity.isEmpty()) {
                throw  new UserNotFoundException("User not found with id: " + req.getUserid());
            }

            // Validate widget
            Optional<widget> widgetEntity = Optional.ofNullable(widgetRepository.findById(req.getWidgetid()));
            if (widgetEntity.isEmpty()) {
                throw new WidgetNotFoundException("Widget not found with id: " + req.getWidgetid());
            }

            // Update fields
            user_widget uw = existingUW.get();
            uw.setUser(userEntity.get());
            uw.setWidget(widgetEntity.get());
            uw.setPos_x(req.getPos_x());
            uw.setPos_y(req.getPos_y());
            uw.setWidth(req.getWidth());
            uw.setHeight(req.getHeight());

            // Save updated entity
            userWidgetRepository.save(uw);
        }

        return ApiResponse.<String>builder()
                .success(true)
                .message("Widgets updated successfully!")
                .data("Total updated: " + requestList.size())
                .build();
    }

    @Transactional
    public ApiResponse<String> DeleteAllUserWidgets(int userid)
    {

        Optional<user> userEntity = userRepository.findById(userid);
        if (userEntity.isEmpty()) {
            throw  new UserNotFoundException("User not found with id: " + userid);
        }

        userWidgetRepository.deleteByUser_Userid(userid);
        return new ApiResponse<>(true, "Successfully deleted", null);
    }

}
