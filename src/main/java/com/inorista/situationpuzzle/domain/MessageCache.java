package com.inorista.situationpuzzle.domain;

import java.time.LocalDateTime;

public class MessageCache {
    protected String channelId;
    protected String messageId;
    protected User author;
    protected LocalDateTime createdAt;

    public String getMessageId() {
        return messageId;
    }

    public String getAuthorId() {
        return author.getUserId();
    }

    public void setAuthorId(String authorId) {
        this.author.setUserId(authorId);
    }

    public void setAuthorName(String name) {
        this.author.setUserName(name);
    }

    public String getAuthorName() {
        return this.author.getUserName();
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

}
