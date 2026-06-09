package com.placementhub.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "resumes")
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    @Column(length = 10000)
    private String resumeText;

    @Column(length = 2000)
    private String extractedSkills;

    private LocalDateTime uploadedAt;

    public Resume() {
        this.uploadedAt = LocalDateTime.now();
    }

    public Resume(String fileName, String resumeText, String extractedSkills) {
        this.fileName = fileName;
        this.resumeText = resumeText;
        this.extractedSkills = extractedSkills;
        this.uploadedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getFileName() { return fileName; }
    public String getResumeText() { return resumeText; }
    public String getExtractedSkills() { return extractedSkills; }
    public LocalDateTime getUploadedAt() { return uploadedAt; }

    public void setId(Long id) { this.id = id; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public void setResumeText(String resumeText) { this.resumeText = resumeText; }
    public void setExtractedSkills(String extractedSkills) { this.extractedSkills = extractedSkills; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
}