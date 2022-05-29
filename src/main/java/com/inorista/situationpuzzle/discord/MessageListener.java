package com.inorista.situationpuzzle.discord;

import discord4j.core.object.entity.Message;
import reactor.core.publisher.Mono;

/**
 * Message listener interface.
 */
public interface MessageListener {

  Mono<Void> processCommand(Message eventMessage);
}
