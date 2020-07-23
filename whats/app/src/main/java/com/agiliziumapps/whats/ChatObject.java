package com.agiliziumapps.whats;

public class ChatObject {

    private String chatId;
    private String name;

    public ChatObject(String chatId) {
        this.chatId = chatId;
    }

    public String getChatId() {
        return chatId;
    }

    public String getName() {
        return name;
    }
}
