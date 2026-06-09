package com.placementhub.dto;

import java.util.List;

public class MatchResponse {

    private String companyName;
    private String roleName;
    private String resumeFileName;
    private double matchScore;
    private List<String> matchedSkills;
    private List<String> missingSkills;

    public MatchResponse() {
    }

    public MatchResponse(String companyName, String roleName, String resumeFileName,
                         double matchScore, List<String> matchedSkills, List<String> missingSkills) {
        this.companyName = companyName;
        this.roleName = roleName;
        this.resumeFileName = resumeFileName;
        this.matchScore = matchScore;
        this.matchedSkills = matchedSkills;
        this.missingSkills = missingSkills;
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
}