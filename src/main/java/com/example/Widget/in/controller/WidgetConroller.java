package com.example.Widget.in.controller;


import com.example.Widget.in.dto.ApiResponse;
import com.example.Widget.in.dto.WidgetRequest;
import com.example.Widget.in.entities.widget;
import com.example.Widget.in.service.WidgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@RestController
@RequestMapping("/api/widget")
public class WidgetConroller {


    @Autowired
    private WidgetService widgetService;



    @GetMapping("/allwidgets")
    public ResponseEntity<ApiResponse<List<widget>>> getAllWidget()
    {
        return ResponseEntity.ok(widgetService.listWidgets());
    }


//    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<ApiResponse<widget>> addWidget(@RequestPart("widget") WidgetRequest widgetRequest, @RequestPart("icon")MultipartFile iconFile) {
//
//        String fileName = null;
//        try {
//            // Directory for storing uploaded widget icons
//            String uploadDir = "uploads/widgets/";
//            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
//
//            if (!Files.exists(uploadPath)) {
//                Files.createDirectories(uploadPath);
//            }
//
//            // Unique filename based on widget code or name
//            String originalFilename = iconFile.getOriginalFilename();
//            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
//            fileName = "Widget_" + widgetRequest.getTitle() + extension;
//
//            Path filePath = uploadPath.resolve(fileName);
//            iconFile.transferTo(filePath.toFile());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            ApiResponse<widget> res = new ApiResponse<>(
//                    false,
//                    "Failed to upload icon: " + e.getMessage(),
//                    null
//            );
////            logger.error("Failed to upload widget icon.");
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
//        }
//
//        boolean added = widgetService.AddWidget(
//                widgetRequest.getTitle(),
//                widgetRequest.getDescription(),
//                "/uploads/widgets/" + fileName,
//                widgetRequest.getDefaultHeight(),
//                widgetRequest.getDefaultWidth()
//        );
//
//        if (added) {
//            ApiResponse<widget> res = new ApiResponse<>(
//                    true,
//                    "Widget added successfully",
//                    null
//            );
////            logger.info("Widget data saved successfully.");
//            return ResponseEntity.ok(res);
//        }
//
//        ApiResponse<widget> res = new ApiResponse<>(
//                false,
//                "Widget already exists",
//                null
//        );
////        logger.warn("Widget creation conflict - already exists.");
//        return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
//
//    }
}
