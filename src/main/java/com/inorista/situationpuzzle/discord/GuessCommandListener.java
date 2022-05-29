package com.inorista.situationpuzzle.discord;

import com.inorista.situationpuzzle.GameManager;
import com.inorista.situationpuzzle.domain.Guess;
import com.inorista.situationpuzzle.domain.Question;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import java.util.Optional;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Guess command (/guess).
 * TODO: better interaction
 */
@Service
public class GuessCommandListener extends SlashCommandListener implements MessageListener {

  public GuessCommandListener(GameManager gameManager) {
    super(gameManager, "guess");
  }

  @Override
  public Class<MessageCreateEvent> getEventType() {
    return MessageCreateEvent.class;
  }

  @Override
  public Mono<Void> processCommand(Message eventMessage) {
    Guess guess = Guess.fromMessage(eventMessage);
    Optional<Question> question = gameManager.getRunningQuestion(
        eventMessage.getChannelId().asString());
    if (question.isEmpty()) {
      return Mono.empty();
    }
    guess.setQuestionId(question.get().getQuestionId());
    try {
      gameManager.guessScenario(guess);
    } catch (Exception e) {
      e.printStackTrace();
      return Mono.error(e);
    }
    return Mono.empty();
  }
}
