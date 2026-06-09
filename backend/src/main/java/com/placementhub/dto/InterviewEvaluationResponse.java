package com.placementhub.dto;

import java.util.List;

public class InterviewEvaluationResponse {

    private int score;
    private List<String> strengths;
    private List<String> improvements;

    public InterviewEvaluationResponse(
            int score,
            List<String> strengths,
            List<String> improvements
    ) {
        this.score = score;
        this.strengths = strengths;
        this.improvements = improvements;
    }

    public int getScore() {
        return score;
    }

    public List<String> getStrengths() {
        return strengths;
    }

    public List<String> getImprovements() {
        return improvements;
    }
}