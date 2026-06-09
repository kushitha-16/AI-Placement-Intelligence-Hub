package com.placementhub.service;

import com.placementhub.dto.InterviewEvaluationResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MockInterviewService {

    public InterviewEvaluationResponse evaluate(String answer) {

        int score = 50;

        List<String> strengths = new ArrayList<>();
        List<String> improvements = new ArrayList<>();

        if (answer.length() > 100) {
            score += 15;
            strengths.add("Detailed answer");
        }

        if (answer.toLowerCase().contains("project")) {
            score += 10;
            strengths.add("Mentioned project experience");
        }

        if (answer.toLowerCase().contains("java")) {
            score += 10;
            strengths.add("Mentioned technical skills");
        }

        if (!answer.toLowerCase().contains("skill")) {
            improvements.add("Mention your technical skills");
        }

        if (!answer.toLowerCase().contains("goal")) {
            improvements.add("Mention career goals");
        }

        if (score > 100) {
            score = 100;
        }

        return new InterviewEvaluationResponse(
                score,
                strengths,
                improvements
        );
    }
}