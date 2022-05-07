package com.inorista.situationpuzzle.discord;

import com.inorista.situationpuzzle.domain.Question;
import discord4j.core.object.entity.Message;
import reactor.core.publisher.Mono;

public interface  MessageListener {
    Mono<Void> processCommand(Message eventMessage);
}
