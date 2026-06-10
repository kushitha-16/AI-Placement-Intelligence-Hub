package com.placementhub.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class InterviewHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;

    private int score;

    private LocalDateTime attemptedAt;

    public InterviewHistory() {
        this.attemptedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public int getScore() {
        return score;
    }

    public LocalDateTime getAttemptedAt() {
        return attemptedAt;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setScore(int score) {
        this.score = score;
    }
}