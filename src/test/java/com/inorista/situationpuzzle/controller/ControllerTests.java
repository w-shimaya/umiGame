package com.inorista.situationpuzzle.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import com.inorista.situationpuzzle.GameManager;
import com.inorista.situationpuzzle.domain.GameSummary;
import com.inorista.situationpuzzle.domain.MessageCache;
import com.inorista.situationpuzzle.domain.Question;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(ResultController.class)
public class ControllerTests {
    @MockBean
    private GameManager gameManager;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void before() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testListController() throws Exception {
        GameSummary summary = new GameSummary();
        Question question = new Question.Builder()
                .questionId(1)
                .channelId("10")
                .createdAt(LocalDateTime.now())
                .messageId("100")
                .authorId("1000")
                .statement("テスト問題")
                .authorName("ユーザーネーム")
                .build();
        question.setQuestionId(1);
        summary.setQuestion(question);
        Mockito.doReturn(Optional.of(summary)).when(gameManager).getGameSummary(1);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/result/list")
                        .param("questionId", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("result"))
                .andReturn();

        Question actual = (Question) result.getModelAndView().getModel().get("question");
        assertEquals(1, actual.getQuestionId());
    }
}
