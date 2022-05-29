package com.inorista.situationpuzzle.domain;

/**
 * Selector class for question.
 */
public class QuestionSelector {

  private String channelId;
  private int questionId;

  public QuestionSelector() {
  }

  public QuestionSelector(String channelId) {
    this.channelId = channelId;
  }

  /**
   * Selector by question ID.
   *
   * @param questionId question ID to find
   * @return selector object
   */
  public static QuestionSelector byQuestionId(int questionId) {
    QuestionSelector selector = new QuestionSelector();
    selector.questionId = questionId;
    return selector;
  }
}
