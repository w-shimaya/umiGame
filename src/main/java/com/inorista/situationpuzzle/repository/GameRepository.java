package com.inorista.situationpuzzle.repository;

import com.inorista.situationpuzzle.domain.Clarification;
import com.inorista.situationpuzzle.domain.ClarificationSelector;
import com.inorista.situationpuzzle.domain.ClarificationState;
import com.inorista.situationpuzzle.domain.Guess;
import com.inorista.situationpuzzle.domain.GuessSelector;
import com.inorista.situationpuzzle.domain.MessageCache;
import com.inorista.situationpuzzle.domain.Question;
import com.inorista.situationpuzzle.domain.QuestionSelector;
import java.util.List;
import java.util.Optional;

/**
 * Repository.
 */
public interface GameRepository {

  List<Question> getAllQuestions();

  List<Question> findQuestion(QuestionSelector selector);

  Optional<Question> findLatestQuestion(String channelId);

  List<Clarification> findClarification(ClarificationSelector selector);

  void updateQuestion(Question question);

  int addMessage(MessageCache message);

  int addQuestion(Question question);

  int addClarification(Clarification clarification);

  int addGuess(Guess guess);

  int updateClarificationState(int clarificationId, ClarificationState answer);

  int updateGuess(int guessId, boolean isCorrect);

  MessageCache findMessage(String messageId);

  List<Guess> findGuess(GuessSelector selector);
}
