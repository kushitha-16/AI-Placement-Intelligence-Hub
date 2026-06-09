package com.placementhub.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_posts")
public class JobPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;

    private String roleName;

    @Column(length = 1000)
    private String eligibility;

    @Column(length = 1000)
    private String requiredSkills;

    @Column(length = 1000)
    private String selectionRounds;

    private String deadline;

    @Column(length = 1000)
    private String applyLink;

    @Column(length = 5000)
    private String originalPost;

    private String applicationStatus = "Not Applied";

    private LocalDateTime createdAt;

    public JobPost() {
        this.createdAt = LocalDateTime.now();
        this.applicationStatus = "Not Applied";
    }

    public JobPost(String companyName, String roleName, String eligibility, String requiredSkills,
                   String selectionRounds, String deadline, String applyLink, String originalPost) {
        this.companyName = companyName;
        this.roleName = roleName;
        this.eligibility = eligibility;
        this.requiredSkills = requiredSkills;
        this.selectionRounds = selectionRounds;
        this.deadline = deadline;
        this.applyLink = applyLink;
        this.originalPost = originalPost;
        this.applicationStatus = "Not Applied";
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
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

    public String getRequiredSkills() {
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

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setRequiredSkills(String requiredSkills) {
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

    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}