package com.inorista.situationpuzzle.discord;

import com.inorista.situationpuzzle.GameManager;
import com.inorista.situationpuzzle.domain.Question;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * /q command listener.
 */
@Service
public class StartQuestionCommandListener extends SlashCommandListener implements MessageListener {

  @Value("${server.hostname}")
  private String hostname;

  private final Logger logger = LoggerFactory.getLogger(StartQuestionCommandListener.class);

  public StartQuestionCommandListener(GameManager gameManager) {
    super(gameManager, "q");
  }

  @Override
  public Class<MessageCreateEvent> getEventType() {
    return MessageCreateEvent.class;
  }

  @Override
  public Mono<Void> processCommand(Message eventMessage) {
    return Mono.just(eventMessage)
        .flatMap(this::startQuestion)
        .flatMap(this::handleError)
        .then();
  }

  private Mono<Message> startQuestion(Message message) {
    Question question = Question.fromMessage(message);
    if (question == null) {
      return Mono.empty();
    }

    try {
      if (gameManager.startWith(question) != 1) {
        logger.error("Failed to start game with question " + question);
        return Mono.just(message);
      }
      logger.info("Question added: " + question);

      message.getChannel().flatMap(channel -> channel.createMessage("スタート:turtle:\nhttp://"
              + hostname
              + "/result/list?questionId="
              + question.getQuestionId()
              + "&includeYes=on&includeNo=on"))
          .subscribe();
      return Mono.empty();
    } catch (Exception e) {
      e.printStackTrace();
      return Mono.just(message);
    }
  }

  private Mono<Void> handleError(Message message) {
    return Mono.just(message)
        .flatMap(Message::getChannel)
        .flatMap(channel ->
            channel.createMessage("問題の登録に失敗しました:sob:\n> "
                + message.getContent().substring("/q ".length())))
        .then();
  }
}
