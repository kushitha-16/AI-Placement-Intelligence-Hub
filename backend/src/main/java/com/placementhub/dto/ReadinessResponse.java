package com.placementhub.dto;

public class ReadinessResponse {

    private int technicalScore;
    private int projectScore;
    private int communicationScore;
    private int overallScore;
    private String status;

    public ReadinessResponse(
            int technicalScore,
            int projectScore,
            int communicationScore,
            int overallScore,
            String status
    ) {
        this.technicalScore = technicalScore;
        this.projectScore = projectScore;
        this.communicationScore = communicationScore;
        this.overallScore = overallScore;
        this.status = status;
    }

    public int getTechnicalScore() {
        return technicalScore;
    }

    public int getProjectScore() {
        return projectScore;
    }

    public int getCommunicationScore() {
        return communicationScore;
    }

    public int getOverallScore() {
        return overallScore;
    }

    public String getStatus() {
        return status;
    }
}