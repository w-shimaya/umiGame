package com.inorista.situationpuzzle.controller;

import com.inorista.situationpuzzle.GameManager;
import com.inorista.situationpuzzle.domain.GameSummary;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String resultList(@RequestParam("questionId") String questionId,
                             Model model) {
        Optional<GameSummary> summary = gameManager.getGameSummary(Integer.parseInt(questionId));
        summary.ifPresent(s -> {
            model.addAttribute("question", s.getQuestion());
            model.addAttribute("history", s.getHistory());
        });
        return "result";
    }
}
