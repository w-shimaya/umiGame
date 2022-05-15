package com.inorista.situationpuzzle.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.inorista.situationpuzzle.domain.Clarification;
import com.inorista.situationpuzzle.domain.ClarificationSelector;
import com.inorista.situationpuzzle.domain.ClarificationState;
import com.inorista.situationpuzzle.domain.GameSummary;
import com.inorista.situationpuzzle.domain.Guess;
import com.inorista.situationpuzzle.domain.GuessSelector;
import com.inorista.situationpuzzle.domain.MessageCache;
import com.inorista.situationpuzzle.domain.Question;
import discord4j.common.util.Snowflake;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@SpringBootTest
@ActiveProfiles({"test"})
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
public class GameRepositoryImplTests {
    @Autowired
    GameRepository gameRepository;

    @Test
    @DatabaseSetup("/testdata/initdata.xml")
    void testFindQuestion() {
        List<Question> list = gameRepository.getAllQuestions();
        assertEquals(2, list.size());
    }

    @Test
    @DatabaseSetup("/testdata/initdata.xml")
    void testFindClarificationByMessageId() {
        String messageId = Snowflake.of(101).asString();
        ClarificationSelector selector = new ClarificationSelector();
        selector.setMessageId(messageId);
        List<Clarification> result = gameRepository.findClarification(selector);

        assertFalse(result.isEmpty());
    }

    @Test
    @DatabaseSetup("/testdata/initdata.xml")
    void testFindClarificationByClarificationId() {
        int clarificationId = 1;
        ClarificationSelector selector = new ClarificationSelector();
        selector.setClarificationId(clarificationId);
        List<Clarification> result = gameRepository.findClarification(selector);

        assertFalse(result.isEmpty());
    }

    @Test
    @DatabaseSetup("/testdata/empty.xml")
    void testAddQuestion() {
        Question question = new Question.Builder()
                .messageId("120")
                .channelId("10")
                .authorId("3000")
                .statement("テスト用問題")
                .createdAt(LocalDateTime.now())
                .build();
        int updatedCount = gameRepository.addQuestion(question);
        assertEquals(1, updatedCount);
        assertTrue(question.getQuestionId() > 0);
    }

    @Test
    @DatabaseSetup("/testdata/empty.xml")
    void testAddClarification() {
        Clarification clarification = new Clarification.Builder()
                .questionId(1)
                .channelId("10")
                .messageId("300")
                .authorId("1000")
                .authorName("ユーザー1")
                .content("テスト用質問")
                .createdAt(LocalDateTime.now())
                .state(ClarificationState.AWAIT)
                .build();

        int updatedCount = gameRepository.addClarification(clarification);
        assertEquals(1, updatedCount);
    }

    @Test
    @DatabaseSetup("/testdata/initdata.xml")
    @ExpectedDatabase(value = "/testdata/afterAddClarificationNewUser.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void testAddClarificationByNewUser() {
        Clarification clarification = new Clarification.Builder()
                .questionId(1)
                .channelId("10")
                .messageId("120")
                .authorId("1004")
                .authorName("ユーザー5")
                .content("新キャラの質問")
                .createdAt(LocalDateTime.of(2022, 5, 5, 11, 11, 11))
                .state(ClarificationState.AWAIT)
                .build();
        int updatedCount = gameRepository.addClarification(clarification);
    }

    @Test
    @DatabaseSetup("/testdata/initdata.xml")
    @ExpectedDatabase(value = "/testdata/afterUpdateClarification.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void testUpdateClarification() {
        int updatedCount = gameRepository.updateClarificationState(2, ClarificationState.NO);
        assertEquals(1, updatedCount);
    }

    @Test
    @DatabaseSetup("/testdata/initdata.xml")
    @ExpectedDatabase(value = "/testdata/afterInsertGuessKnown.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void testInsertGuessByKnownUser() {
        Guess guess = new Guess.Builder()
                .guessId(2)
                .questionId(1)
                .authorId("1001")
                .authorName("ユーザー2")
                .messageId(Snowflake.of(120).asString())
                .channelId(Snowflake.of(10).asString())
                .content("テスト用ストーリー予想2")
                .createdAt(LocalDateTime.of(2022, 5, 5, 12, 0, 0))
                .build();

        int updateCount = gameRepository.addGuess(guess);

        assertEquals(1, updateCount);
    }

    @Test
    @DatabaseSetup("/testdata/initdata.xml")
    @ExpectedDatabase(value = "/testdata/afterInsertGuessNew.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void testInsertGuessByNewUser() {
        Guess guess = new Guess.Builder()
                .guessId(2)
                .questionId(1)
                .authorId("1004")
                .authorName("ユーザー5")
                .messageId("120")
                .channelId("10")
                .content("テスト用ストーリー予想2")
                .createdAt(LocalDateTime.of(2022, 5, 5, 10, 30, 0))
                .build();
        int updateCount = gameRepository.addGuess(guess);
    }


    @Test
    @DatabaseSetup("/testdata/afterInsertGuessKnown.xml")
    @ExpectedDatabase(value = "/testdata/afterUpdateGuess.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void testUpdateGuess() {
        int updatedCount = gameRepository.updateGuess(2, false);
        assertEquals(1, updatedCount);
    }

    @Test
    @DatabaseSetup("/testdata/afterInsertGuessKnown.xml")
    void testFindGuessByMessageId() {
        Snowflake messageId = Snowflake.of(120);
        Guess guess = new Guess.Builder()
                .guessId(2)
                .questionId(2)
                .messageId(messageId.asString())
                .channelId("20")
                .content("テスト用ストーリー予想2")
                .createdAt(LocalDateTime.of(2022, 5, 5, 12, 0, 0))
                .build();

        GuessSelector selector = new GuessSelector();
        selector.setMessageId(messageId.asString());
        List<Guess> result = gameRepository.findGuess(selector);
        assertEquals(guess.getGuessId(), result.get(0).getGuessId());
    }

    @Test
    @DatabaseSetup("/testdata/initdata.xml")
    @ExpectedDatabase(value = "/testdata/afterUpdateQuestion.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    void testUpdateQuestion() {
        Question question = new Question.Builder()
                .questionId(1)
                .channelId("10")
                .messageId("100")
                .authorId("1000")
                .statement("水平思考パズル")
                .createdAt(LocalDateTime.of(2022, 5, 3, 21, 8, 0))
                .isAnswered(true)
                .build();

        gameRepository.updateQuestion(question);
    }
    /*
    @Test
    @DatabaseSetup("/testdata/initdata.xml")
    void testGetGameSummary() {
        int questionId = 1;
        Optional<GameSummary> summary = gameRepository.getGameSummary(questionId);

        assertTrue(summary.isPresent());
        assertEquals(questionId, summary.get().getQuestion().getQuestionId());
        assertEquals(3, summary.get().getHistory().size());
        MessageCache front = summary.get().getHistory().get(0);
        MessageCache next = summary.get().getHistory().get(1);
        assertTrue(front.getCreatedAt().isAfter(next.getCreatedAt()));

    }
     */

}
