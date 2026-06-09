package com.placementhub.dto;

import java.util.List;

public class InterviewResponse {

    private String companyName;
    private String roleName;
    private List<String> technicalQuestions;
    private List<String> hrQuestions;
    private List<String> projectQuestions;

    public InterviewResponse(String companyName, String roleName,
                             List<String> technicalQuestions,
                             List<String> hrQuestions,
                             List<String> projectQuestions) {
        this.companyName = companyName;
        this.roleName = roleName;
        this.technicalQuestions = technicalQuestions;
        this.hrQuestions = hrQuestions;
        this.projectQuestions = projectQuestions;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getRoleName() {
        return roleName;
    }

    public List<String> getTechnicalQuestions() {
        return technicalQuestions;
    }

    public List<String> getHrQuestions() {
        return hrQuestions;
    }

    public List<String> getProjectQuestions() {
        return projectQuestions;
    }
}