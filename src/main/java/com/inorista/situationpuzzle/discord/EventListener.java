package com.inorista.situationpuzzle.discord;

import discord4j.core.event.domain.Event;
import reactor.core.publisher.Mono;

/**
 * Discord event listener interface.
 *
 * @param <T> Discord event type
 */
public interface EventListener<T extends Event> {

  Class<T> getEventType();

  Mono<Void> execute(T event);

  default Mono<Void> handleError(Throwable error) {
    error.printStackTrace();
    return Mono.empty();
  }
}
