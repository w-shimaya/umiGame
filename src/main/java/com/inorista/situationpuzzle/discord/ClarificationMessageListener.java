package com.inorista.situationpuzzle.discord;

import com.inorista.situationpuzzle.GameManager;
import com.inorista.situationpuzzle.domain.Clarification;
import com.inorista.situationpuzzle.domain.Question;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import java.util.Optional;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ClarificationMessageListener implements MessageListener, EventListener<MessageCreateEvent> {
    private final GameManager gameManager;

    public ClarificationMessageListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public Class<MessageCreateEvent> getEventType() {
        return MessageCreateEvent.class;
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        // process command only if a game is running
        return Mono.just(event)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> !message.getContent().startsWith("/")) // ignore commands
                .filter(message -> message.getAuthor().map(user -> !user.isBot())
                        .orElse(false)) // ignore bots
                .filter(message -> gameManager.isGameRunning(message.getChannelId().asString()))
                .flatMap(this::processCommand)
                .then();
    }

    @Override
    public Mono<Void> processCommand(Message eventMessage) {
        Clarification clarification = Clarification.fromMessage(eventMessage);
        Optional<Question> question = gameManager.getRunningQuestion(eventMessage.getChannelId().asString());
        if (question.isEmpty()) {
            // log
            return Mono.empty();
        }
        clarification.setQuestionId(question.get().getQuestionId());

        try {
            gameManager.askClarification(clarification);
        } catch (Exception e) {
            e.printStackTrace();
            return Mono.error(e);
        }
        return Mono.empty();
    }
}
