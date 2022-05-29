package com.inorista.situationpuzzle.domain;

/**
 * Selector class for guess table.
 */
public class GuessSelector {

  private String messageId;
  private int guessId;
  private int questionId;

  public int getGuessId() {
    return guessId;
  }

  public void setGuessId(int guessId) {
    this.guessId = guessId;
  }

  public String getMessageId() {
    return messageId;
  }

  public void setMessageId(String messageId) {
    this.messageId = messageId;
  }

  public int getQuestionId() {
    return questionId;
  }

  public void setQuestionId(int questionId) {
    this.questionId = questionId;
  }

  /**
   * Create selector object for finding guesses by questionId.
   *
   * @param questionId question ID
   * @return selector object
   */
  public static GuessSelector byQuestionId(int questionId) {
    GuessSelector selector = new GuessSelector();
    selector.setQuestionId(questionId);
    return selector;
  }

  /**
   * Create selector object for finding a guess by messageId.
   *
   * @param messageId messageId
   * @return selector object
   */
  public static GuessSelector byMessageId(String messageId) {
    GuessSelector selector = new GuessSelector();
    selector.setMessageId(messageId);
    return selector;
  }
}
