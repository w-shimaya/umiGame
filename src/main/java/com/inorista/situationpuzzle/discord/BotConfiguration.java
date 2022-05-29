package com.inorista.situationpuzzle.discord;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Configuration class related to discord client.
 */
@Configuration
@Profile({"prod"})
public class BotConfiguration {

  @Value("${discord.token}")
  private String token;

  /**
   * Register GatewayDiscordClient bean.
   *
   * @param eventListeners event listeners
   * @param <T>            event type
   * @return client
   */
  @Bean
  public <T extends Event> GatewayDiscordClient gateWayDiscordClient(
      List<EventListener<T>> eventListeners) {
    GatewayDiscordClient client = DiscordClientBuilder.create(token)
        .build()
        .login()
        .block();

    for (EventListener<T> listener : eventListeners) {
      client.on(listener.getEventType())
          .flatMap(listener::execute)
          .onErrorResume(listener::handleError)
          .subscribe();
    }

    return client;
  }
}
