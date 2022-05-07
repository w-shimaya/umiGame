package com.inorista.situationpuzzle;

import com.inorista.situationpuzzle.domain.Clarification;
import com.inorista.situationpuzzle.domain.ClarificationSelector;
import com.inorista.situationpuzzle.domain.ClarificationState;
import com.inorista.situationpuzzle.domain.GameSummary;
import com.inorista.situationpuzzle.domain.Guess;
import com.inorista.situationpuzzle.domain.GuessSelector;
import com.inorista.situationpuzzle.domain.Question;
import com.inorista.situationpuzzle.repository.GameRepository;
import discord4j.common.util.Snowflake;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class GameManager {
    private final GameRepository questionRepository;

    public GameManager(GameRepository gameRepository) {
        this.questionRepository = gameRepository;
    }

    public boolean isGameRunning(String channelId) {
        return getRunningQuestion(channelId).isPresent();
    }

    public Optional<Question> getRunningQuestion(String channelId) {
        // assert a game is running on the channel
        return questionRepository
                .findLatestQuestion(channelId)
                .filter(q -> !q.isAnswered());
    }

    public int startWith(Question question) {
        if (isGameRunning(question.getChannelId())) {
            return 0;
        }
        return questionRepository.addQuestion(question);
    }

    public void finish(String channelId) {
        Optional<Question> question = questionRepository.findLatestQuestion(channelId);
        if (question.isEmpty()) {
            throw new RuntimeException();
        }
        Question questionNotEmpty = question.get();
        questionNotEmpty.setAnswered(true);
        questionRepository.updateQuestion(questionNotEmpty);
    }

    public void guessScenario(Guess guess) {
        questionRepository.addGuess(guess);
    }

    public List<Guess> answerGuess(String messageId, boolean isCorrect) {
        GuessSelector selector = new GuessSelector();
        selector.setMessageId(messageId);
        List<Guess> guess = questionRepository.findGuess(selector);
        if (!guess.isEmpty()) {
            questionRepository.updateGuess(guess.get(0).getGuessId(), isCorrect);
            if (isCorrect) {
                finish(guess.get(0).getChannelId());
            }
        }
        return guess;
    }

    public void askClarification(Clarification clarification) {
        questionRepository.addClarification(clarification);
    }

    public void answerClarification(Snowflake messageId, ClarificationState answer) {
        ClarificationSelector selector = new ClarificationSelector();
        selector.setMessageId(messageId.asString());
        List<Clarification> clarifications = questionRepository.findClarification(selector);
        if (!clarifications.isEmpty()) {
            questionRepository.updateClarificationState(clarifications.get(0).getClarificationId(),
                    answer);
        }
    }

    public Optional<GameSummary> getGameSummary(int questionId) {
        return questionRepository.getGameSummary(questionId);
    }
}
