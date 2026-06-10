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

        String lowerAnswer = answer.toLowerCase();

        int score = 40;

        List<String> strengths = new ArrayList<>();
        List<String> improvements = new ArrayList<>();

        if (answer.length() > 50) {
            score += 10;
            strengths.add("Answer has basic detail");
        }

        if (answer.length() > 120) {
            score += 10;
            strengths.add("Answer is detailed and well explained");
        }

        if (lowerAnswer.contains("pursuing") || lowerAnswer.contains("student") || lowerAnswer.contains("cse")) {
            score += 10;
            strengths.add("Mentioned education background");
        }

        if (lowerAnswer.contains("java") || lowerAnswer.contains("python") || lowerAnswer.contains("sql") || lowerAnswer.contains("coding")) {
            score += 10;
            strengths.add("Mentioned technical skills");
        }

        if (lowerAnswer.contains("project") || lowerAnswer.contains("developed") || lowerAnswer.contains("application")) {
            score += 10;
            strengths.add("Mentioned project or development experience");
        }

        if (lowerAnswer.contains("team") || lowerAnswer.contains("communication")) {
            score += 5;
            strengths.add("Mentioned teamwork or communication");
        }

        if (lowerAnswer.contains("goal") || lowerAnswer.contains("career") || lowerAnswer.contains("growth")) {
            score += 5;
            strengths.add("Mentioned career goal or growth mindset");
        }

        if (!lowerAnswer.contains("project")) {
            improvements.add("Add one project example to make your answer stronger.");
        }

        if (!lowerAnswer.contains("java") && !lowerAnswer.contains("python") && !lowerAnswer.contains("sql")) {
            improvements.add("Mention your technical skills like Java, Python, SQL, or web development.");
        }

        if (!lowerAnswer.contains("goal") && !lowerAnswer.contains("career")) {
            improvements.add("End with your career goal or what kind of role you are looking for.");
        }

        if (answer.length() < 80) {
            improvements.add("Make your answer slightly longer with education, skills, project, and goal.");
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