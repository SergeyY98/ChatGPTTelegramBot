package com.example.chatGPT.configs;

public interface ChatGPTProvider {

  String getOpenAiUrl();

  String getOpenAiToken();

  String getModel();

  int getMaxTokens();
}
