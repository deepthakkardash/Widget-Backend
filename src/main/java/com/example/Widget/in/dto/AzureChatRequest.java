package com.example.Widget.in.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class AzureChatRequest {

    private String message;
    private List<Object> conversationHistory;
    private Object pageContext;  // optional


}
