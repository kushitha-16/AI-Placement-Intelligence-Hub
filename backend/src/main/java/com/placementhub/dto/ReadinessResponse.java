package com.placementhub.dto;

import java.util.List;

public class ReadinessResponse {

    private double technicalScore;
    private double projectScore;
    private double resumeScore;
    private double overallScore;
    private String readinessLevel;
    private List<String> recommendations;

    public ReadinessResponse(
            double technicalScore,
            double projectScore,
            double resumeScore,
            double overallScore,
            String readinessLevel,
            List<String> recommendations
    ) {
        this.technicalScore = technicalScore;
        this.projectScore = projectScore;
        this.resumeScore = resumeScore;
        this.overallScore = overallScore;
        this.readinessLevel = readinessLevel;
        this.recommendations = recommendations;
    }

    public double getTechnicalScore() {
        return technicalScore;
    }

    public double getProjectScore() {
        return projectScore;
    }

    public double getResumeScore() {
        return resumeScore;
    }

    public double getOverallScore() {
        return overallScore;
    }

    public String getReadinessLevel() {
        return readinessLevel;
    }

    public List<String> getRecommendations() {
        return recommendations;
    }
}