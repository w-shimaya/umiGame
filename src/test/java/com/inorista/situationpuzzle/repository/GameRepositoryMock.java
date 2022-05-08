package com.inorista.situationpuzzle.repository;

import com.inorista.situationpuzzle.domain.Clarification;
import com.inorista.situationpuzzle.domain.ClarificationSelector;
import com.inorista.situationpuzzle.domain.ClarificationState;
import com.inorista.situationpuzzle.domain.GameSummary;
import com.inorista.situationpuzzle.domain.Guess;
import com.inorista.situationpuzzle.domain.GuessSelector;
import com.inorista.situationpuzzle.domain.MessageCache;
import com.inorista.situationpuzzle.domain.Question;
import com.inorista.situationpuzzle.domain.QuestionSelector;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameRepositoryMock implements GameRepository {
    private List<Question> questions;
    private List<Guess> guesses;
    private List<Clarification> clarifications;

    @Override
    public int addMessage(MessageCache message) {
        return 1;
    }

    public GameRepositoryMock() {
        questions = new ArrayList<>();
        guesses = new ArrayList<>();
        clarifications = new ArrayList<>();
    }

    @Override
    public int updateClarificationState(int clarificationId, ClarificationState answer) {
        return 1;
    }

    @Override
    public void updateQuestion(Question question) {

    }

    @Override
    public int addGuess(Guess guess) {
        guesses.add(guess);
        return 1;
    }

    @Override
    public List<Question> getAllQuestions() {
        return this.questions;
    }

    @Override
    public List<Question> findQuestion(QuestionSelector selector) {
        return null;
    }

    @Override
    public Optional<Question> findLatestQuestion(String channelId) {
        if (questions.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(questions.get(questions.size() - 1));
    }

    @Override
    public List<Clarification> findClarification(ClarificationSelector selector) {
        return new ArrayList<>();
    }

    @Override
    public int addQuestion(Question question) {
        questions.add(question);
        return 1;
    }

    @Override
    public int updateGuess(int guessId, boolean isCorrect) {
        return 1;
    }

    @Override
    public MessageCache findMessage(String messageId) {
        return null;
    }

    @Override
    public List<Guess> findGuess(GuessSelector selector) {
        return null;
    }

    @Override
    public Optional<GameSummary> getGameSummary(int questionId) {
        return Optional.empty();
    }

    @Override
    public int addClarification(Clarification clarification) {
        clarifications.add(clarification);
        return 1;
    }
}
