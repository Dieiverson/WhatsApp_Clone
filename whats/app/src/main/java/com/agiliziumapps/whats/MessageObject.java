package com.agiliziumapps.whats;

public class MessageObject
{
    String messageId;
    String text;
    String senderId;

    public MessageObject(String messageId, String text, String senderId) {
        this.messageId = messageId;
        this.text = text;
        this.senderId = senderId;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getText() {
        return text;
    }
}
