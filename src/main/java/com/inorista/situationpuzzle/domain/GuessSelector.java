package com.inorista.situationpuzzle.domain;

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

    public static GuessSelector byQuestionId(int questionId) {
        GuessSelector selector = new GuessSelector();
        selector.setQuestionId(questionId);
        return selector;
    }

    public static GuessSelector byMessageId(String messageId) {
        GuessSelector selector = new GuessSelector();
        selector.setMessageId(messageId);
        return selector;
    }
}
