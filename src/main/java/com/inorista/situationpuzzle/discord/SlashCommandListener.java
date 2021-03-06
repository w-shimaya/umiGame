package com.inorista.situationpuzzle.discord;

import com.inorista.situationpuzzle.GameManager;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import reactor.core.publisher.Mono;

/**
 * Abstract class to implement slash commands.
 */
public abstract class SlashCommandListener implements EventListener<MessageCreateEvent> {

  protected String commandName;
  protected GameManager gameManager;

  public SlashCommandListener(GameManager gameManager, String commandName) {
    this.gameManager = gameManager;
    this.commandName = commandName;
  }

  /**
   * Decide if the message should be processed.
   *
   * @param event message create event
   * @return empty mono.
   */
  public Mono<Void> execute(MessageCreateEvent event) {
    return Mono.just(event)
        .map(MessageCreateEvent::getMessage)
        .filter(message -> message.getAuthor()
            .map(user -> !user.isBot()).orElse(false))
        .filter(message -> message.getContent().startsWith("/" + commandName + " "))
        .flatMap(this::processCommand)
        .then();
  }

  public abstract Mono<Void> processCommand(Message message);
}
