package com.inorista.situationpuzzle.controller;

import com.inorista.situationpuzzle.GameManager;
import com.inorista.situationpuzzle.controller.form.FilterForm;
import com.inorista.situationpuzzle.domain.ClarificationState;
import com.inorista.situationpuzzle.domain.GameSummary;
import com.inorista.situationpuzzle.domain.GuessState;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/result")
public class ResultController {
    private final GameManager gameManager;

    public ResultController(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @GetMapping("/list")
    public String resultList(
            @RequestParam("questionId") String questionId,
            @Validated FilterForm filterForm,
            Model model) {
        List<ClarificationState> clarificationFilter = new ArrayList<>();
        List<GuessState> guessFilter = new ArrayList<>();

        if (filterForm.isIncludeYes()) {
            clarificationFilter.add(ClarificationState.YES);
            guessFilter.add(GuessState.CORRECT);
        }
        if (filterForm.isIncludeNo()) {
            clarificationFilter.add(ClarificationState.NO);
            guessFilter.add(GuessState.WRONG);
        }
        if (filterForm.isIncludeAwait()) {
            clarificationFilter.add(ClarificationState.AWAIT);
            guessFilter.add(GuessState.AWAIT);
        }
        if (filterForm.isIncludeVague()) {
            clarificationFilter.add(ClarificationState.VAGUE);
        }
        
        model.addAttribute("filterForm", filterForm);

        Optional<GameSummary> summary = gameManager.getGameSummary(
                Integer.parseInt(questionId),
                clarificationFilter,
                guessFilter);
        summary.ifPresent(s -> {
            model.addAttribute("question", s.getQuestion());
            model.addAttribute("history", s.getHistory());
        });
        return "result";
    }
}
