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
import com.inorista.situationpuzzle.domain.UserSelector;
import com.inorista.situationpuzzle.repository.mybatis.QuestionMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GameRepositoryImpl implements GameRepository {
    private final SqlSessionTemplate sqlSession;

    public GameRepositoryImpl(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public Optional<Question> findLatestQuestion(String channelId) {
        QuestionSelector selector = new QuestionSelector(channelId);
        List<Question> qList = sqlSession.getMapper(QuestionMapper.class).findQuestion(selector);
        if (qList.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(qList.get(0));
    }

    @Override
    public List<Clarification> findClarification(ClarificationSelector selector) {
        return sqlSession.getMapper(QuestionMapper.class).findClarification(selector);
    }

    @Override
    public List<Question> getAllQuestions() {
        return sqlSession.getMapper(QuestionMapper.class).findQuestion(null);
    }

    @Override
    public void updateQuestion(Question question) {
        sqlSession.getMapper(QuestionMapper.class).updateQuestionState(question);
    }

    @Override
    public int addMessage(MessageCache message) {
        return sqlSession.getMapper(QuestionMapper.class).insertMessage(message);
    }

    @Override
    public int addQuestion(Question question) {
        QuestionMapper mapper = sqlSession.getMapper(QuestionMapper.class);
        if (mapper.findUser(UserSelector.byUserId(question.getAuthorId())).isEmpty()) {
            mapper.insertUser(question.getAuthor());
        }
        mapper.insertMessage(question);
        return sqlSession.getMapper(QuestionMapper.class).insertQuestion(question);
    }

    @Override
    public int addClarification(Clarification clarification) {
        QuestionMapper mapper = sqlSession.getMapper(QuestionMapper.class);
        if (mapper.findUser(UserSelector.byUserId(clarification.getAuthorId()))
                .isEmpty()) {
            mapper.insertUser(clarification.getAuthor());
        }
        mapper.insertMessage(clarification);
        return sqlSession.getMapper(QuestionMapper.class).insertClarification(clarification);
    }

    @Override
    public int addGuess(Guess guess) {
        QuestionMapper mapper = sqlSession.getMapper(QuestionMapper.class);
        if (mapper.findUser(UserSelector.byUserId(guess.getAuthorId())).isEmpty()) {
            mapper.insertUser(guess.getAuthor());
        }
        mapper.insertMessage(guess);
        return mapper.insertGuess(guess);
    }

    @Override
    public int updateClarificationState(int clarificationId, ClarificationState answer) {
        ClarificationSelector selector = new ClarificationSelector();
        selector.setClarificationId(clarificationId);
        List<Clarification> clarification = sqlSession.getMapper(QuestionMapper.class)
                .findClarification(selector);
        if (clarification.isEmpty()) {
            return 0;
        }
        Clarification clarificationForUpdate = clarification.get(0);
        clarificationForUpdate.setState(answer);
        return sqlSession.getMapper(QuestionMapper.class).updateClarificationState(clarificationForUpdate);
    }

    @Override
    public int updateGuess(int guessId, boolean isCorrect) {
        return sqlSession.getMapper(QuestionMapper.class).updateGuess(guessId, isCorrect);
    }

    @Override
    public MessageCache findMessage(String messageId) {
        return null;
    }

    @Override
    public List<Guess> findGuess(GuessSelector selector) {
        return sqlSession.getMapper(QuestionMapper.class).findGuess(selector);
    }

    @Override
    public Optional<GameSummary> getGameSummary(int questionId) {
        GameSummary summary = new GameSummary();
        QuestionMapper mapper = sqlSession.getMapper(QuestionMapper.class);
        List<Question> questions = mapper.findQuestion(QuestionSelector.byQuestionId(questionId));
        if (questions.isEmpty()) {
            return Optional.empty();
        }
        // questions.size() must be 1
        summary.setQuestion(questions.get(0));

        List<Clarification> clarifications = mapper.findClarification(ClarificationSelector.byQuestionId(questionId));
        List<Guess> guesses = mapper.findGuess(GuessSelector.byQuestionId(questionId));

        List<MessageCache> history = new ArrayList<>();
        history.addAll(clarifications);
        history.addAll(guesses);
        history.sort((m1, m2) -> - m1.getCreatedAt().compareTo(m2.getCreatedAt()));
        summary.setHistory(history);
        return Optional.of(summary);
    }
}
