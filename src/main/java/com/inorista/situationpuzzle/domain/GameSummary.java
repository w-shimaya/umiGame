package com.inorista.situationpuzzle.domain;

import java.util.List;

/**
 * Game summary domain.
 */
public class GameSummary {

  private List<MessageCache> history;
  private Question question;

  public List<MessageCache> getHistory() {
    return history;
  }

  public void setHistory(List<MessageCache> history) {
    this.history = history;
  }

  public Question getQuestion() {
    return question;
  }

  public void setQuestion(Question question) {
    this.question = question;
  }
}
