package com.example.Widget.in.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AzureChatResponse {

    private boolean success;
    private String response;
    private List<Map<String, Object>> conversationHistory;
    private Map<String, Object> usage;
    private String method;
    private String error;


}
