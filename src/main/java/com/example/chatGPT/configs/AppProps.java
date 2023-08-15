package com.example.chatGPT.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "application")
@Data
public class AppProps implements ChatGPTProvider, TelegramBotProvider {
  private String botName;

  private String telegramToken;

  private String chatId;

  private String openAiUrl;

  private String openAiToken;

  private String model;

  private int maxTokens;

  @Override
  public String getOpenAiUrl() {
    return openAiUrl;
  }

  @Override
  public String getTelegramToken() {
    return telegramToken;
  }

  @Override
  public String getBotName() {
    return botName;
  }

  @Override
  public String getOpenAiToken() {
    return openAiToken;
  }

  @Override
  public String getModel() {
    return model;
  }

  @Override
  public int getMaxTokens() {
    return maxTokens;
  }
}
