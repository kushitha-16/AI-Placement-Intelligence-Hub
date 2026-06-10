package com.placementhub.service;

import com.placementhub.dto.InterviewEvaluationResponse;
import com.placementhub.model.InterviewHistory;
import com.placementhub.repository.InterviewHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MockInterviewService {

    private final InterviewHistoryRepository historyRepository;

    public MockInterviewService(InterviewHistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

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

        InterviewHistory history = new InterviewHistory();
        history.setQuestion("Mock Interview");
        history.setScore(score);
        historyRepository.save(history);

        return new InterviewEvaluationResponse(
                score,
                strengths,
                improvements
        );
    }
}