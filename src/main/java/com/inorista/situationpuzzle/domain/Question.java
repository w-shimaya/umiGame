package com.inorista.situationpuzzle.domain;

import discord4j.core.object.entity.Message;
import java.time.LocalDateTime;

/**
 * Question domain.
 */
public class Question extends MessageCache {

  private int questionId;
  private String statement;
  private boolean isAnswered;
  private String answer;

  /**
   * Create a question object based on a discord message.
   *
   * @param message discord message
   * @return question object
   */
  public static Question fromMessage(Message message) {
    Question question = new Question();
    int idxSpace = message.getContent().indexOf(" ");
    question.statement = message.getContent().substring(idxSpace + 1);
    question.channelId = message.getChannelId().asString();
    question.messageId = message.getId().asString();
    if (message.getAuthor().isPresent()) {
      question.setAuthor(new User());
      question.setAuthorId(message.getAuthor().get().getId().asString());
      question.setAuthorName(message.getAuthor().get().getUsername());
    } else {
      return null;
    }
    question.isAnswered = false;
    question.createdAt = LocalDateTime.now();
    return question;
  }

  public boolean isAnswered() {
    return isAnswered;
  }

  public void setAnswered(boolean answered) {
    isAnswered = answered;
  }

  public int getQuestionId() {
    return questionId;
  }

  public void setQuestionId(int questionId) {
    this.questionId = questionId;
  }

  public String getStatement() {
    return statement;
  }

  public void setStatement(String statement) {
    this.statement = statement;
  }

  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }

  @Override
  public String toString() {
    return questionId + ": " + statement;
  }

  /**
   * Util class to build a question object.
   */
  public static class Builder {

    private final Question question;

    public Builder() {
      question = new Question();
      question.setAuthor(new User());
    }

    public Question build() {
      return question;
    }

    public Builder questionId(int id) {
      question.setQuestionId(id);
      return this;
    }

    public Builder statement(String statement) {
      question.setStatement(statement);
      return this;
    }

    public Builder channelId(String channelId) {
      question.setChannelId(channelId);
      return this;
    }

    public Builder messageId(String messageId) {
      question.setMessageId(messageId);
      return this;
    }

    public Builder authorId(String authorId) {
      question.setAuthorId(authorId);
      return this;
    }

    public Builder authorName(String authorName) {
      question.setAuthorName(authorName);
      return this;
    }

    public Builder isAnswered(boolean isAnswered) {
      question.setAnswered(isAnswered);
      return this;
    }

    public Builder answer(String answer) {
      question.setAnswer(answer);
      return this;
    }

    public Builder createdAt(LocalDateTime createdAt) {
      question.setCreatedAt(createdAt);
      return this;
    }
  }
}
