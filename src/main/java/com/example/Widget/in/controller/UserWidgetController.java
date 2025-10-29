package com.example.Widget.in.controller;


import com.example.Widget.in.dto.ApiResponse;
import com.example.Widget.in.dto.UserWidgetRequest;
import com.example.Widget.in.dto.UserWidgetResponse;
import com.example.Widget.in.service.UserwidgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/userwidget")
public class UserWidgetController {

    @Autowired
    private UserwidgetService userwidgetService;

    @GetMapping("/{userid}")
    public ResponseEntity<ApiResponse<List<UserWidgetResponse>>> getUserWidgets(@PathVariable int userid)
    {
        return ResponseEntity.ok(userwidgetService.getusersAllWidget(userid));
    }

    @PostMapping("/AddWidgets")
    public ResponseEntity<ApiResponse<String>> AddUserWidgets(@RequestBody UserWidgetRequest[] userwidgetRequest)
    {
        return ResponseEntity.ok(userwidgetService.AddUserWidget(userwidgetRequest));
    }

    @PostMapping("/updateWidget")
    public ResponseEntity<ApiResponse<String>> UpdateUserWidget(@RequestBody UserWidgetRequest[] userwidgetRequest)
    {
        return ResponseEntity.ok(userwidgetService.UpdateUserWidget(userwidgetRequest));
    }

    @DeleteMapping("/deleteUserWidget/{user_widget_id}")
    public ResponseEntity<ApiResponse<String>> DeleteUserWidget(@PathVariable int user_widget_id)
    {
        return ResponseEntity.ok(userwidgetService.DeleteUserWidget(user_widget_id));
    }

    @DeleteMapping("/deleteAllUserWidget/{userid}")
    public ResponseEntity<ApiResponse<String>> DeleteAllUserWidgets(@PathVariable int userid)
    {
        return ResponseEntity.ok(userwidgetService.DeleteAllUserWidgets(userid));
    }
}
