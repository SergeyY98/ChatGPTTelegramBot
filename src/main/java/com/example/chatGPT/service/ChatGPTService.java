package com.example.chatGPT.service;

import com.example.chatGPT.configs.ChatGPTProvider;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class ChatGPTService {

  private final ChatGPTProvider provider;

  @Autowired
  public ChatGPTService(ChatGPTProvider provider) { this.provider = provider; }

  public Mono<String> askChatGPTText(String message) {
    WebClient webClient = WebClient.builder()
        .baseUrl(provider.getOpenAiUrl())
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + provider.getOpenAiToken())
        .build();

    String prompt = "User: " + message + "\nAI:";
    JSONObject request = new JSONObject();
    request.put("prompt", prompt);
    request.put("max_tokens", provider.getMaxTokens());
    request.put("model", provider.getModel());

    return webClient.post()
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(request.toString()))
        .retrieve()
        .bodyToMono(String.class);
  }
}
