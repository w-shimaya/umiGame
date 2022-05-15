package com.inorista.situationpuzzle;

import com.inorista.situationpuzzle.domain.Clarification;
import com.inorista.situationpuzzle.domain.ClarificationSelector;
import com.inorista.situationpuzzle.domain.ClarificationState;
import com.inorista.situationpuzzle.domain.GameSummary;
import com.inorista.situationpuzzle.domain.Guess;
import com.inorista.situationpuzzle.domain.GuessSelector;
import com.inorista.situationpuzzle.domain.GuessState;
import com.inorista.situationpuzzle.domain.MessageCache;
import com.inorista.situationpuzzle.domain.Question;
import com.inorista.situationpuzzle.domain.QuestionSelector;
import com.inorista.situationpuzzle.repository.GameRepository;
import discord4j.common.util.Snowflake;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.checkerframework.checker.nullness.Opt;
import org.springframework.stereotype.Service;
import reactor.util.annotation.Nullable;

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

    public void answerClarification(Snowflake messageId, String reactionUserId, ClarificationState answer) {
        ClarificationSelector selector = new ClarificationSelector();
        selector.setMessageId(messageId.asString());
        Optional<Clarification> clarification = questionRepository.findClarification(selector).stream().findFirst();

        if (clarification.isPresent()) {
            Optional<String> questionAuthorId = questionRepository
                    .findQuestion(QuestionSelector.byQuestionId(clarification.get().getQuestionId()))
                    .stream()
                    .findFirst()
                    .map(Question::getAuthorId);
            if (questionAuthorId.isPresent() && questionAuthorId.get().equals(reactionUserId)) {
                questionRepository.updateClarificationState(clarification.get().getClarificationId(),
                        answer);
            }
        }
    }

    public Optional<GameSummary> getGameSummary(
            int questionId,
            List<ClarificationState> includeClarifications,
            List<GuessState> includeGuesses) {
        GameSummary summary = new GameSummary();
        List<Question> questions = questionRepository.findQuestion(QuestionSelector.byQuestionId(questionId));
        if (questions.isEmpty()) {
            return Optional.empty();
        }

        summary.setQuestion(questions.get(0));

        List<Clarification> clarifications =
                questionRepository.findClarification(ClarificationSelector.byQuestionId(questionId));
        List<Guess> guess = questionRepository.findGuess(GuessSelector.byQuestionId(questionId));

        List<MessageCache> history = new ArrayList<>();
        history.addAll(clarifications.stream()
                .filter(c -> includeClarifications.contains(c.getState()))
                .collect(Collectors.toList()));
        history.addAll(guess.stream()
                .filter(g -> includeGuesses.contains(g.getState()))
                .collect(Collectors.toList()));
        history.sort((m1, m2) -> -m1.getCreatedAt().compareTo(m2.getCreatedAt()));
        summary.setHistory(history);
        return Optional.of(summary);
    }
}
