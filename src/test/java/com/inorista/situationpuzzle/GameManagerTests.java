package com.inorista.situationpuzzle;

import static org.junit.jupiter.api.Assertions.*;
import com.inorista.situationpuzzle.domain.Clarification;
import com.inorista.situationpuzzle.domain.ClarificationState;
import com.inorista.situationpuzzle.domain.GameSummary;
import com.inorista.situationpuzzle.domain.Guess;
import com.inorista.situationpuzzle.domain.MessageCache;
import com.inorista.situationpuzzle.domain.Question;
import com.inorista.situationpuzzle.repository.GameRepository;
import com.inorista.situationpuzzle.repository.GameRepositoryMock;
import discord4j.common.util.Snowflake;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


class GameManagerTests {
    @Nested
    class StatefulTests {
        private GameRepository mockRepository;

        @BeforeEach
        void before() {
            mockRepository = new GameRepositoryMock();
            MockitoAnnotations.openMocks(this);
        }

        @Test
        void testStartGame() {
            Snowflake channelId = Snowflake.of(1);
            Question question = new Question();
            question.setChannelId(channelId.asString());
            GameManager manager = new GameManager(mockRepository);

            manager.startWith(question);

            assertTrue(manager.isGameRunning(channelId.asString()));
        }

        @Test
        void testCannotStartGame() {
            Snowflake channelId = Snowflake.of(1);
            Question runningQuestion = new Question.Builder()
                    .questionId(1)
                    .channelId(channelId.asString())
                    .build();
            GameManager manager = new GameManager(mockRepository);
            manager.startWith(runningQuestion);

            manager.startWith(new Question.Builder()
                    .questionId(2)
                    .channelId(channelId.asString())
                    .build());

            assertEquals(runningQuestion, mockRepository.findLatestQuestion(channelId.asString()).get());
        }

        @Test
        void testCheckGameBeforeFirstTime() {
            Snowflake channelId = Snowflake.of(1);
            GameManager manager = new GameManager(mockRepository);

            assertFalse(manager.isGameRunning(channelId.asString()));
        }

        @Test
        void testFinishGame() {
            Snowflake channelId = Snowflake.of(1);
            Question question = new Question();
            question.setChannelId(channelId.asString());
            GameManager manager = new GameManager(mockRepository);

            manager.startWith(question);
            boolean afterStart = manager.isGameRunning(channelId.asString());
            manager.finish(channelId.asString());

            assertTrue(afterStart);
            assertFalse(manager.isGameRunning(channelId.asString()));
        }
    }

    @Nested
    class StatelessTests {
        @Mock
        private GameRepository gameRepository;

        @BeforeEach
        void before() {
            MockitoAnnotations.openMocks(this);
        }

        @Test
        void testAddGuess() {
            Guess guess = new Guess();
            Mockito.doReturn(1).when(gameRepository)
                    .addGuess(guess);
            GameManager manager = new GameManager(gameRepository);

            manager.guessScenario(guess);

            Mockito.verify(gameRepository, Mockito.times(1))
                    .addGuess(guess);
        }

        @Test
        void testAskClarification() {
            Clarification clarification = new Clarification();
            Mockito.doReturn(1).when(gameRepository)
                    .addClarification(clarification);
            GameManager manager = new GameManager(gameRepository);

            manager.askClarification(clarification);

            Mockito.verify(gameRepository, Mockito.times(1))
                    .addClarification(clarification);
        }

        @Test
        void testAnswerClarification() {
            int clarificationId = 10;
            int questionId = 1;
            Snowflake userId = Snowflake.of(20);
            Snowflake messageId = Snowflake.of(10);
            ClarificationState answer = ClarificationState.YES;
            Clarification clarification = new Clarification();
            clarification.setClarificationId(clarificationId);
            clarification.setMessageId(messageId.asString());
            clarification.setState(ClarificationState.AWAIT);
            Question question = new Question.Builder()
                    .questionId(questionId)
                    .authorId(userId.asString())
                    .build();

            Mockito.doReturn(List.of(question)).when(gameRepository)
                    .findQuestion(ArgumentMatchers.any());
            Mockito.doReturn(List.of(clarification)).when(gameRepository)
                    .findClarification(ArgumentMatchers.any());
            Mockito.doReturn(1).when(gameRepository)
                    .updateClarificationState(clarificationId, answer);
            GameManager manager = new GameManager(gameRepository);

            manager.answerClarification(messageId, userId.asString(), answer);

            Mockito.verify(gameRepository, Mockito.times(1))
                    .updateClarificationState(clarificationId, answer);
        }

        @Test
        void testGuessSucceed() {
            // setup: guess message
            int guessId = 1;
            Snowflake messageId = Snowflake.of(2);
            Snowflake channelId = Snowflake.of(3);
            Guess guess = new Guess.Builder()
                    .guessId(guessId)
                    .messageId(messageId.asString())
                    .channelId(channelId.asString())
                    .build();
            MessageCache message = new MessageCache();
            message.setMessageId(messageId.asString());
            message.setChannelId(channelId.asString());
            // setup: question message
            Question question = new Question();
            question.setChannelId(channelId.asString());
            // setup: mock and target
            GameManager manager = new GameManager(gameRepository);

            Mockito.doReturn(List.of(guess)).when(gameRepository)
                    .findGuess(ArgumentMatchers.any());
            Mockito.doReturn(message).when(gameRepository)
                    .findMessage(messageId.asString());
            Mockito.doReturn(Optional.of(question)).when(gameRepository)
                    .findLatestQuestion(channelId.asString());
            Mockito.doNothing().when(gameRepository)
                    .updateQuestion(question);

            manager.startWith(question);
            boolean beforeFinish = manager.isGameRunning(channelId.asString());
            manager.answerGuess(messageId.asString(), true);

            assertTrue(beforeFinish);
            Mockito.verify(gameRepository, Mockito.times(1))
                    .updateQuestion(question);
            assertFalse(manager.isGameRunning(channelId.asString()));
        }

        @Test
        void testGuessFail() {
            Snowflake channelId = Snowflake.of(1);
            Snowflake messageId = Snowflake.of(2);
            int guessId = 3;
            Guess guess = new Guess.Builder()
                    .guessId(guessId)
                    .messageId(messageId.asString())
                    .build();
            Question unansweredQuestion = new Question();
            unansweredQuestion.setAnswered(false);
            unansweredQuestion.setMessageId(messageId.asString());
            GameManager manager = new GameManager(gameRepository);
            Mockito.doReturn(List.of(guess)).when(gameRepository)
                    .findGuess(ArgumentMatchers.any());
            Mockito.doReturn(Optional.of(unansweredQuestion)).when(gameRepository)
                    .findLatestQuestion(channelId.asString());

            manager.answerGuess(messageId.asString(), false);

            Mockito.verify(gameRepository, Mockito.times(1))
                    .updateGuess(guessId, false);
            Mockito.verify(gameRepository, Mockito.times(0))
                    .updateQuestion(ArgumentMatchers.any());
            assertTrue(manager.isGameRunning(channelId.asString()));
        }

        @Test
        void testGetSummary() {
            int questionId = 1;
            GameSummary summary = new GameSummary();
            summary.setQuestion(new Question());
            summary.getQuestion().setQuestionId(questionId);
            // mock
            Mockito.doReturn(Optional.of(summary)).when(gameRepository)
                    .getGameSummary(questionId);

            GameManager manager = new GameManager(gameRepository);
            Optional<GameSummary> result = manager.getGameSummary(questionId);

            // 問題と解答履歴を出す
            assertEquals(summary.getQuestion().getQuestionId(),
                    result.get().getQuestion().getQuestionId());
        }

        @Test
        void testReactionNotByAuthor() {
            Snowflake messageId = Snowflake.of(1);
            String authorId = "2";
            String nonAuthorId = "3";
            int clarificationId = 4;
            Question question = new Question.Builder()
                    .authorId(authorId)
                    .build();
            Clarification clarification = new Clarification.Builder()
                    .clarificationId(clarificationId)
                    .build();
            Mockito.doReturn(List.of(clarification)).when(gameRepository)
                    .findClarification(ArgumentMatchers.any());
            Mockito.doReturn(1).when(gameRepository)
                    .updateClarificationState(clarificationId, ClarificationState.NO);
            Mockito.doReturn(List.of(question)).when(gameRepository)
                    .findQuestion(ArgumentMatchers.any());
            GameManager manager = new GameManager(gameRepository);

            manager.answerClarification(messageId, nonAuthorId, ClarificationState.NO);

            Mockito.verify(gameRepository, Mockito.times(0))
                    .updateClarificationState(ArgumentMatchers.anyInt(), ArgumentMatchers.any());
        }
    }
}
