package com.example.Widget.in.service;


import com.example.Widget.in.dto.AzureChatRequest;
import com.example.Widget.in.dto.AzureChatResponse;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

//TODO give meaning full name "ChatBotService"
@Service
public class AzureOpenAIService {

    @Value("${azure.openai.endpoint}")
    private String openaiEndpoint;

    @Value("${azure.openai.deployment}")
    private String deployment;

    @Value("${azure.openai.api.key}")
    private String apiKey;

    @Value("${azure.search.endpoint:}")
    private String searchEndpoint;

    @Value("${azure.search.index:}")
    private String searchIndex;

    @Value("${azure.search.key:}")
    private String searchKey;

    private final WebClient webClient = WebClient.builder().build();

    public Mono<AzureChatResponse> sendMessage(AzureChatRequest chatRequest) {

        try {
            String url = String.format(
                    "%s/openai/deployments/%s/chat/completions?api-version=2024-05-01-preview",
                    openaiEndpoint, deployment
            );

            // Build messages
            List<Map<String, Object>> messages = new ArrayList<>();
            messages.add(Map.of("role", "system",
                    "content", "You are an AI assistant that helps users find information based solely on the provided data. " +
                            "If there isn't enough information, you will let them know to ask questions related to the containers and its details."));
            messages.add(Map.of("role", "user", "content", chatRequest.getMessage()));  //TODO Trim the message

            // Build request body
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", deployment);
            requestBody.put("messages", messages);
            requestBody.put("max_tokens", 16384);
            requestBody.put("temperature", 0.7);
            requestBody.put("top_p", 0.95);
            requestBody.put("frequency_penalty", 0);
            requestBody.put("presence_penalty", 0);
            requestBody.put("stream", false);

            // Add Azure Search if configured
            if (searchEndpoint != null && !searchEndpoint.isEmpty() &&
                    searchIndex != null && !searchIndex.isEmpty() &&
                    searchKey != null && !searchKey.isEmpty()) {

                Map<String, Object> searchParams = new HashMap<>();
                searchParams.put("type", "azure_search");
                searchParams.put("parameters", Map.of(
                        "endpoint", searchEndpoint,
                        "index_name", searchIndex,
                        "semantic_configuration", "default",
                        "query_type", "vector_semantic_hybrid",
                        "in_scope", true,
                        "strictness", 3,
                        "top_n_documents", 5,
                        "authentication", Map.of(
                                "type", "api_key",
                                "key", searchKey
                        ),
                        "embedding_dependency", Map.of(
                                "type", "deployment_name",
                                "deployment_name", "text-embedding-ada-002"
                        )
                ));

                requestBody.put("data_sources", List.of(searchParams));
            }

            return webClient.post()
                    .uri(url)
                    .header("Content-Type", "application/json")
                    .header("api-key", apiKey)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .map(response -> {
                        AzureChatResponse azurechatResponse = new AzureChatResponse();
                        Map<String, Object> choice = ((List<Map<String, Object>>) response.get("choices")).get(0);
                        Map<String, Object> message = (Map<String, Object>) choice.get("message");  //TODO Need to debug and check the values <"role", "system/user/assistant">

                        azurechatResponse.setSuccess(true);
                        azurechatResponse.setSuccess(true); //TODO remove duplicate line
                        azurechatResponse.setResponse((String) message.get("content"));
                        azurechatResponse.setConversationHistory(updateHistory(chatRequest.getConversationHistory(),message));
                        azurechatResponse.setUsage((Map<String, Object>) response.get("usage"));
                        azurechatResponse.setMethod("direct-http");

                        return azurechatResponse;
                    })

                    .onErrorResume(WebClientResponseException.class, ex -> {
                        AzureChatResponse errorResponse = new AzureChatResponse();
                        errorResponse.setSuccess(false);
                        errorResponse.setError(ex.getMessage());
                        errorResponse.setResponse("I'm sorry, I encountered an error while processing your request.");
                        return Mono.just(errorResponse);
                    });
        } catch (Exception e) {
            AzureChatResponse errorResponse = new AzureChatResponse();
            errorResponse.setSuccess(false);
            errorResponse.setError(e.getMessage());
            errorResponse.setResponse("I'm sorry, I encountered an error while processing your request.");
            return Mono.just(errorResponse);
        }


    }


    private List<Map<String, Object>> updateHistory(List<Object> converstionHistory, Map<String, Object> message){
        List<Map<String, Object>> updatedHistory = new ArrayList<>();

        // Add all previous messages if exist
        if (converstionHistory != null) {
            for (Object obj : converstionHistory) {
                if (obj instanceof Map) {
                    updatedHistory.add((Map<String, Object>) obj);
                }
            }
        }

        // Add the new assistant message
                updatedHistory.add(message);

        return updatedHistory;
    }
}
