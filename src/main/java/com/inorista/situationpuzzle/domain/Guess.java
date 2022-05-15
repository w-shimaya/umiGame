package com.inorista.situationpuzzle.domain;

import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Message;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.Optional;
import javax.swing.text.html.Option;
import org.springframework.cglib.core.Local;

public class Guess extends MessageCache implements ViewTableRow {
    private String content;
    private boolean isCorrect;
    private boolean isAnswered;
    private int guessId;
    private int questionId;

    public static Guess fromMessage(Message message) {
        Guess guess = new Guess();
        guess.content = message.getContent().substring("/guess ".length());
        guess.isCorrect = false;
        guess.isAnswered = false;
        guess.setAuthor(new User());
        message.getAuthor().ifPresent(user -> {
            guess.setAuthorName(user.getUsername());
            guess.setAuthorId(user.getId().asString());
        });
        guess.messageId = message.getId().asString();
        guess.channelId = message.getChannelId().asString();
        guess.createdAt = LocalDateTime.ofInstant(message.getId().getTimestamp(),
                ZoneId.of("Asia/Tokyo"));
        return guess;
    }

    public boolean isAnswered() {
        return isAnswered;
    }

    public void setAnswered(boolean answered) {
        isAnswered = answered;
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
        if (isAnswered) {
            return isCorrect ? "YES" : "NO";
        } else {
            return "未回答";
        }
    }

    @Override
    public String getRowCsvClass() {
        if (isAnswered) {
            return isCorrect ? "table-success" : "table-danger";
        } else {
            return "";
        }
    }

    public GuessState getState() {
        if (isAnswered) {
            return isCorrect ? GuessState.CORRECT : GuessState.WRONG;
        } else {
            return GuessState.AWAIT;
        }
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public int getGuessId() {
        return guessId;
    }

    public void setGuessId(int guessId) {
        this.guessId = guessId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public static class Builder {
        private Guess guess;

        public Builder() {
            guess = new Guess();
            guess.setAuthor(new User());
        }

        public Builder guessId(int id) {
            guess.setGuessId(id);
            return this;
        }

        public Builder questionId(int id) {
            guess.setQuestionId(id);
            return this;
        }

        public Builder messageId(String id) {
            guess.setMessageId(id);
            return this;
        }

        public Builder channelId(String id) {
            guess.setChannelId(id);
            return this;

        }

        public Builder content(String content) {
            guess.setContent(content);
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            guess.setCreatedAt(createdAt);
            return this;
        }

        public Builder isCorrect(boolean isCorrect) {
            guess.setCorrect(isCorrect);
            return this;
        }

        public Builder authorId(String authorId) {
            guess.setAuthorId(authorId);
            return this;
        }

        public Builder authorName(String name) {
            guess.setAuthorName(name);
            return this;
        }

        public Guess build() {
            return guess;
        }
    }
}
