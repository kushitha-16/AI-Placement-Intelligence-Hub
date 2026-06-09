package com.placementhub.dto;

import java.util.List;

public class JobAnalyzeResponse {

    private Long jobPostId;
    private String companyName;
    private String roleName;
    private String eligibility;
    private List<String> requiredSkills;
    private String selectionRounds;
    private String deadline;
    private String applyLink;
    private String originalPost;

    public JobAnalyzeResponse() {
    }

    public JobAnalyzeResponse(Long jobPostId, String companyName, String roleName, String eligibility,
                              List<String> requiredSkills, String selectionRounds, String deadline,
                              String applyLink, String originalPost) {
        this.jobPostId = jobPostId;
        this.companyName = companyName;
        this.roleName = roleName;
        this.eligibility = eligibility;
        this.requiredSkills = requiredSkills;
        this.selectionRounds = selectionRounds;
        this.deadline = deadline;
        this.applyLink = applyLink;
        this.originalPost = originalPost;
    }

    public Long getJobPostId() {
        return jobPostId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getRoleName() {
        return roleName;
    }

    public String getEligibility() {
        return eligibility;
    }

    public List<String> getRequiredSkills() {
        return requiredSkills;
    }

    public String getSelectionRounds() {
        return selectionRounds;
    }

    public String getDeadline() {
        return deadline;
    }

    public String getApplyLink() {
        return applyLink;
    }

    public String getOriginalPost() {
        return originalPost;
    }

    public void setJobPostId(Long jobPostId) {
        this.jobPostId = jobPostId;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setEligibility(String eligibility) {
        this.eligibility = eligibility;
    }

    public void setRequiredSkills(List<String> requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    public void setSelectionRounds(String selectionRounds) {
        this.selectionRounds = selectionRounds;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public void setApplyLink(String applyLink) {
        this.applyLink = applyLink;
    }

    public void setOriginalPost(String originalPost) {
        this.originalPost = originalPost;
    }
}
