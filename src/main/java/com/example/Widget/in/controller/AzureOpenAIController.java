package com.example.Widget.in.controller;

import com.example.Widget.in.dto.AzureChatRequest;
import com.example.Widget.in.dto.AzureChatResponse;
import com.example.Widget.in.service.AzureOpenAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.function.ToDoubleBiFunction;

@RestController
@RequestMapping("/api/openai")
@CrossOrigin(origins = "*")
//TODO  give it meaning full name "ChatBotController"
public class AzureOpenAIController {

    @Autowired
    private AzureOpenAIService azureOpenAIService;

    @PostMapping("/chat")
    public Mono<AzureChatResponse> chat(@RequestBody AzureChatRequest azurechatRequest) {

        return azureOpenAIService.sendMessage(azurechatRequest);
    }
}

// ye frontend se call hoga
//const response = await fetch('https://your-backend/api/openai/chat', {
//    method: 'POST',
//            headers: { 'Content-Type': 'application/json' },
//    body: JSON.stringify({
//            message: userInput,
//            conversationHistory: []
//  })
//});

