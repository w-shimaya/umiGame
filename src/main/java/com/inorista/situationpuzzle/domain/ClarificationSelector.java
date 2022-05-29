package com.inorista.situationpuzzle.domain;

/**
 * Selector class.
 */
public class ClarificationSelector {

  private Integer clarificationId;
  private String messageId;
  private Integer questionId;

  public Integer getClarificationId() {
    return clarificationId;
  }

  public void setClarificationId(Integer clarificationId) {
    this.clarificationId = clarificationId;
  }

  public String getMessageId() {
    return messageId;
  }

  public void setMessageId(String messageId) {
    this.messageId = messageId;
  }

  public Integer getQuestionId() {
    return questionId;
  }

  public void setQuestionId(Integer questionId) {
    this.questionId = questionId;
  }

  /**
   * Find clarification by a question ID.
   *
   * @param questionId question ID
   * @return selector object
   */
  public static ClarificationSelector byQuestionId(int questionId) {
    ClarificationSelector selector = new ClarificationSelector();
    selector.setQuestionId(questionId);
    return selector;
  }

  /**
   * Find clarification by a message ID.
   *
   * @param messageId message ID
   * @return selector object
   */
  public static ClarificationSelector byMessageId(String messageId) {
    ClarificationSelector selector = new ClarificationSelector();
    selector.setMessageId(messageId);
    return selector;
  }
}
