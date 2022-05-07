package com.inorista.situationpuzzle.repository.mybatis;

import com.inorista.situationpuzzle.domain.Clarification;
import com.inorista.situationpuzzle.domain.ClarificationSelector;
import com.inorista.situationpuzzle.domain.GameSummary;
import com.inorista.situationpuzzle.domain.Guess;
import com.inorista.situationpuzzle.domain.GuessSelector;
import com.inorista.situationpuzzle.domain.MessageCache;
import com.inorista.situationpuzzle.domain.Question;
import com.inorista.situationpuzzle.domain.QuestionSelector;
import com.inorista.situationpuzzle.domain.User;
import com.inorista.situationpuzzle.domain.UserSelector;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import reactor.util.annotation.Nullable;

@Mapper
public interface QuestionMapper {
    List<Question> findQuestion(@Nullable QuestionSelector selector);

    int insertMessage(MessageCache message);

    int insertQuestion(Question question);

    int updateQuestionState(Question question);

    int insertClarification(Clarification clarification);

    Optional<User> findUser(UserSelector selector);

    List<Clarification> findClarification(ClarificationSelector selector);

    int updateClarificationState(Clarification clarification);

    int insertUser(User user);

    int insertGuess(Guess guess);

    int updateGuess(int guessId, boolean isCorrect);

    List<Guess> findGuess(GuessSelector selector);

}
