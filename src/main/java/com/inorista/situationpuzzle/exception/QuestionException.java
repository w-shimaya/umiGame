package com.inorista.situationpuzzle.exception;

import discord4j.core.object.entity.Message;
import reactor.core.publisher.Flux;

public class QuestionException extends Exception {
    private final Message message;

    public QuestionException(Message message) {
        this.message = message;
    }

    public Message getDiscordMessage() {
        return message;
    }

    public void reportError() {
        Flux.just(message)
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage("問題の登録に失敗しました:sob:"))
                .subscribe();
    }
}
