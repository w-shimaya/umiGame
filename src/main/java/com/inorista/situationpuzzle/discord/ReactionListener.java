package com.inorista.situationpuzzle.discord;

import com.inorista.situationpuzzle.GameManager;
import com.inorista.situationpuzzle.domain.AvailableEmoji;
import com.inorista.situationpuzzle.domain.Clarification;
import com.inorista.situationpuzzle.domain.ClarificationState;
import com.inorista.situationpuzzle.domain.Guess;
import com.inorista.situationpuzzle.domain.Question;
import discord4j.core.event.domain.message.ReactionAddEvent;
import discord4j.core.object.entity.channel.Channel;
import discord4j.core.object.reaction.ReactionEmoji;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ReactionListener implements EventListener<ReactionAddEvent> {
    private final GameManager gameManager;

    public ReactionListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public Class<ReactionAddEvent> getEventType() {
        return ReactionAddEvent.class;
    }

    @Override
    public Mono<Void> execute(ReactionAddEvent event) {
        return Mono.just(event)
                .filter(e -> {
                    Optional<Question> runningQuestion = gameManager.getRunningQuestion(e.getChannelId().asString());
                    return runningQuestion.filter(
                                    // 出題者によるリアクションのみ
                                    question -> question.getAuthorId().equals(e.getUserId().asString())
                                            // 出題後のメッセージに対するリアクションのみ
                                            && question.getCreatedAt().isBefore(
                                            LocalDateTime.ofInstant(e.getMessageId().getTimestamp(),
                                                    ZoneId.of("Asia/Tokyo"))))
                            .isPresent();
                })
                .flatMap(this::processCommand)
                .then();
    }

    private Mono<Void> processCommand(ReactionAddEvent event) {
        // a は x/o じゃなければ何でもいい
        ReactionEmoji.Unicode emoji = event.getEmoji().asUnicodeEmoji()
                .orElse(ReactionEmoji.Unicode.unicode("a"));
        if (emoji.equals(AvailableEmoji.CROSS.getReactionEmoji())) {
            gameManager.answerClarification(event.getMessageId(), ClarificationState.NO);
            gameManager.answerGuess(event.getMessageId().asString(), false);
        } else if (emoji.equals(AvailableEmoji.CIRCLE.getReactionEmoji())) {
            gameManager.answerClarification(event.getMessageId(), ClarificationState.YES);
            List<Guess> answeredGuess = gameManager
                    .answerGuess(event.getMessageId().asString(), true);
            if (!answeredGuess.isEmpty()) {
                event.getChannel().flatMap(ch -> ch.createMessage(answeredGuess.get(0).getAuthor().getUserName()
                                + "さんに10FP!"))
                        .subscribe();
            }

        }
        return Mono.empty();
    }
}
