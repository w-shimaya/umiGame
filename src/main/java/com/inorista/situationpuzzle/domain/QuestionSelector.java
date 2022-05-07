package com.inorista.situationpuzzle.domain;

import discord4j.common.util.Snowflake;

public class QuestionSelector {
    private String channelId;
    private int questionId;

    public QuestionSelector() {
    }

    public QuestionSelector(String channelId) {
        this.channelId = channelId;
    }

    public static QuestionSelector byQuestionId(int questionId) {
        QuestionSelector selector = new QuestionSelector();
        selector.questionId = questionId;
        return selector;
    }
}
