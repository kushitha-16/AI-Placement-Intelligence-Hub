package com.placementhub.dto;

import java.util.List;

public class MatchResponse {

    private String companyName;
    private String roleName;
    private String resumeFileName;
    private double matchScore;
    private List<String> matchedSkills;
    private List<String> missingSkills;
    private List<String> studyPlan;
    private List<String> resumeSuggestions;

    public MatchResponse() {
    }

    public MatchResponse(String companyName, String roleName, String resumeFileName,
                         double matchScore, List<String> matchedSkills,
                         List<String> missingSkills, List<String> studyPlan,
                         List<String> resumeSuggestions) {
        this.companyName = companyName;
        this.roleName = roleName;
        this.resumeFileName = resumeFileName;
        this.matchScore = matchScore;
        this.matchedSkills = matchedSkills;
        this.missingSkills = missingSkills;
        this.studyPlan = studyPlan;
        this.resumeSuggestions = resumeSuggestions;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getRoleName() {
        return roleName;
    }

    public String getResumeFileName() {
        return resumeFileName;
    }

    public double getMatchScore() {
        return matchScore;
    }

    public List<String> getMatchedSkills() {
        return matchedSkills;
    }

    public List<String> getMissingSkills() {
        return missingSkills;
    }

    public List<String> getStudyPlan() {
        return studyPlan;
    }

    public List<String> getResumeSuggestions() {
        return resumeSuggestions;
    }
}