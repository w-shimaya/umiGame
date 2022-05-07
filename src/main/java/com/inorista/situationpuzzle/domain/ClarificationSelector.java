package com.inorista.situationpuzzle.domain;

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

    public static ClarificationSelector byQuestionId(int questionId) {
        ClarificationSelector selector = new ClarificationSelector();
        selector.setQuestionId(questionId);
        return selector;
    }
}
