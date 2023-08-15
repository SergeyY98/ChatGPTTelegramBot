package com.example.chatGPT.bot;

import com.example.chatGPT.configs.TelegramBotProvider;
import com.example.chatGPT.service.ChatGPTService;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class ChatGPTTelegramBot extends TelegramLongPollingBot {

  private final TelegramBotProvider telegramBotProvider;

  private final ChatGPTService chatGPTService;

  @Autowired
  public ChatGPTTelegramBot(TelegramBotProvider telegramBotProvider, ChatGPTService chatGPTService) {
    this.telegramBotProvider = telegramBotProvider;
    this.chatGPTService = chatGPTService;
  }

  @Override
  public String getBotUsername() { return telegramBotProvider.getBotName(); }

  @Override
  public String getBotToken() { return telegramBotProvider.getTelegramToken(); }

  @Override
  public void onUpdateReceived(@NotNull Update update) {
    log.info("Update received: {}", update);
    if (update.hasMessage() && update.getMessage().hasText()) {
      String messageText = update.getMessage().getText();
      long chatId = update.getMessage().getChatId();

      SendMessage outMessage = new SendMessage();
      outMessage.setChatId(String.valueOf(chatId));
      chatGPTService.askChatGPTText(update.getMessage().getText())
          .subscribe(stringObject -> {
            JSONObject jsonObject = new JSONObject(stringObject);
            JSONArray choicesArray = jsonObject.getJSONArray("choices");
            String textValue = "No choices found for prompt: " + messageText;
            if (choicesArray.length() > 0) {
              JSONObject firstChoice = choicesArray.getJSONObject(0);
              textValue = firstChoice.getString("text");
              log.info("Extracted text: " + textValue);
            }
            outMessage.setText(textValue);
            try {
              execute(outMessage);
            } catch (TelegramApiException e) {
              e.printStackTrace();
            }
          });
    }
  }
}
