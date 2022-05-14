package com.inorista.situationpuzzle.domain;

import discord4j.core.object.entity.Message;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class Clarification extends MessageCache implements ViewTableRow {
    private int questionId;
    private int clarificationId;
    private String content;
    private ClarificationState state;


    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getClarificationId() {
        return clarificationId;
    }

    public void setClarificationId(int clarificationId) {
        this.clarificationId = clarificationId;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String getTime() {
        return createdAt.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                .withLocale(Locale.JAPAN));
    }

    @Override
    public String getStatus() {
        return switch (state) {
            case YES -> "YES";
            case NO -> "NO";
            case AWAIT -> "未回答";
        };
    }

    @Override
    public String getRowCsvClass() {
        return "";
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ClarificationState getState() {
        return state;
    }

    public void setState(ClarificationState state) {
        this.state = state;
    }

    public static Clarification fromMessage(Message message) {
        Clarification clarification = new Clarification();
        clarification.channelId = message.getChannelId().asString();
        clarification.messageId = message.getId().asString();
        clarification.setAuthor(new User());
        if (message.getAuthor().isEmpty()) {
            throw new RuntimeException();
        }
        clarification.setAuthorId(message.getAuthor().get().getId().asString());
        clarification.setAuthorName(message.getAuthor().get().getUsername());
        clarification.content = message.getContent();
        clarification.state = ClarificationState.AWAIT;
        clarification.createdAt = LocalDateTime.ofInstant(message.getTimestamp(),
                ZoneId.of("Asia/Tokyo"));
        return clarification;
    }

    public static class Builder {
        Clarification clarification;

        public Builder() {
            clarification = new Clarification();
            clarification.setAuthor(new User());
        }

        public Clarification build() {
            return clarification;
        }

        public Builder questionId(int id) {
            clarification.setQuestionId(id);
            return this;
        }

        public Builder clarificationId(int id) {
            clarification.setClarificationId(id);
            return this;
        }

        public Builder channelId(String id) {
            clarification.setChannelId(id);
            return this;
        }

        public Builder messageId(String id) {
            clarification.setMessageId(id);
            return this;
        }

        public Builder authorId(String id) {
            clarification.setAuthorId(id);
            return this;
        }

        public Builder authorName(String name) {
            clarification.setAuthorName(name);
            return this;
        }

        public Builder content(String content) {
            clarification.setContent(content);
            return this;
        }

        public Builder state(ClarificationState state) {
            clarification.setState(state);
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            clarification.setCreatedAt(createdAt);
            return this;
        }
    }
}
