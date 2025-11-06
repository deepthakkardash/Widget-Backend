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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.Widget.in.entities.user_widget;
import com.example.Widget.in.entities.user;
import com.example.Widget.in.entities.widget;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
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

    private static final Logger logger= LoggerFactory.getLogger(UserwidgetService.class);


    private Integer getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof org.springframework.security.core.userdetails.UserDetails userDetails) {
            String username = userDetails.getUsername();
            user foundUser = userRepository.findByUsername(username);
            if (foundUser == null) {
                throw new UserNotFoundException("User Not Exists");
            }
            return foundUser.getUserid();

        } else if (principal instanceof user customUser) {
            return customUser.getUserid();
        } else {
            throw new RuntimeException("Unexpected principal type: " + principal.getClass().getName());
        }
    }




    public ApiResponse<List<UserWidgetResponse>> getusersAllWidget() {
        Integer userid = getAuthenticatedUserId();
        logger.error("USER ID : {}" , userid);

        List<UserWidgetResponse> reslist = userWidgetRepository.findByUser_Userid(userid)
                .stream()
                .map(uw -> UserWidgetResponse.builder()
                        .user_widget_id(uw.getUser_widget_id())
                        .userid(uw.getUser().getUserid())
                        .widgetid(uw.getWidget().getWidgetid())
                        .pos_x(uw.getPos_x())
                        .pos_y(uw.getPos_y())
                        .width(uw.getWidth())
                        .height(uw.getHeight())
                        .build())
                .collect(Collectors.toList());

        return new ApiResponse<>(true, "Successfully fetched", reslist);
    }



    public ApiResponse<String> AddUserWidget(List<UserWidgetRequest> requestList) {
        Integer userId = getAuthenticatedUserId();

        // Fetch user once
        user userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        // Collect all requested widget IDs
        List<Integer> widgetIds = requestList.stream()
                .map(UserWidgetRequest::getWidgetid)
                .distinct()
                .toList();

        // Fetch all widgets in one query
        List<widget> widgets = widgetRepository.findAllById(widgetIds);

        // Convert to a map for quick lookup
        Map<Integer, widget> widgetMap = widgets.stream()
                .collect(Collectors.toMap(widget::getWidgetid, w -> w));

        // Validate that all requested widget IDs exist
        List<Integer> missingIds = widgetIds.stream()
                .filter(id -> !widgetMap.containsKey(id))
                .toList();

        if (!missingIds.isEmpty()) {
            throw new WidgetNotFoundException("Widget(s) not found with ids: " + missingIds);
        }

        // Build entities in one pass
        List<user_widget> entities = requestList.stream()
                .map(req -> user_widget.builder()
                        .user(userEntity)
                        .widget(widgetMap.get(req.getWidgetid()))
                        .pos_x(req.getPos_x())
                        .pos_y(req.getPos_y())
                        .width(req.getWidth())
                        .height(req.getHeight())
                        .build())
                .toList();

        // Save all at once
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



    public ApiResponse<String> UpdateUserWidget(List<UserWidgetRequest> requestList) {
        Integer userId = getAuthenticatedUserId();

        // Fetch authenticated user once
        user userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        // Collect all IDs to fetch in batch
        List<Integer> userWidgetIds = requestList.stream()
                .map(UserWidgetRequest::getUser_widget_id)
                .toList();
        List<Integer> widgetIds = requestList.stream()
                .map(UserWidgetRequest::getWidgetid)
                .distinct()
                .toList();

        // Fetch all relevant user_widget and widget entities
        Map<Integer, user_widget> userWidgetMap = userWidgetRepository.findAllById(userWidgetIds)
                .stream().collect(Collectors.toMap(user_widget::getUser_widget_id, uw -> uw));

        Map<Integer, widget> widgetMap = widgetRepository.findAllById(widgetIds)
                .stream().collect(Collectors.toMap(widget::getWidgetid, w -> w));

        // Validate all IDs exist
        List<Integer> missingUW = userWidgetIds.stream()
                .filter(id -> !userWidgetMap.containsKey(id))
                .toList();
        if (!missingUW.isEmpty()) {
            throw new UserWidgetNotFoundException("UserWidget(s) not found with ids: " + missingUW);
        }

        List<Integer> missingWidgets = widgetIds.stream()
                .filter(id -> !widgetMap.containsKey(id))
                .toList();
        if (!missingWidgets.isEmpty()) {
            throw new WidgetNotFoundException("Widget(s) not found with ids: " + missingWidgets);
        }

        // Apply updates
        List<user_widget> updatedEntities = requestList.stream()
                .map(req -> {
                    user_widget uw = userWidgetMap.get(req.getUser_widget_id());
                    uw.setUser(userEntity);
                    uw.setWidget(widgetMap.get(req.getWidgetid()));
                    uw.setPos_x(req.getPos_x());
                    uw.setPos_y(req.getPos_y());
                    uw.setWidth(req.getWidth());
                    uw.setHeight(req.getHeight());
                    return uw;
                })
                .toList();

        // Save all updated entities in one call
        userWidgetRepository.saveAll(updatedEntities);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Widgets updated successfully!")
                .data("Total updated: " + updatedEntities.size())
                .build();
    }


    @Transactional
    public ApiResponse<String> DeleteAllUserWidgets()
    {

        Optional<user> userEntity = userRepository.findById(getAuthenticatedUserId());
        if (userEntity.isEmpty()) {
            throw  new UserNotFoundException("User not found with id: " + getAuthenticatedUserId());
        }

        userWidgetRepository.deleteByUser_Userid(getAuthenticatedUserId());
        return new ApiResponse<>(true, "Successfully deleted", null);
    }

}
