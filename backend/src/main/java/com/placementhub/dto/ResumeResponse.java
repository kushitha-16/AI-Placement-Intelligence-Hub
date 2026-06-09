package com.placementhub.dto;

import java.util.List;

public class ResumeResponse {

    private Long resumeId;
    private String fileName;
    private List<String> extractedSkills;

    public ResumeResponse() {
    }

    public ResumeResponse(Long resumeId, String fileName, List<String> extractedSkills) {
        this.resumeId = resumeId;
        this.fileName = fileName;
        this.extractedSkills = extractedSkills;
    }

    public Long getResumeId() {
        return resumeId;
    }

    public String getFileName() {
        return fileName;
    }

    public List<String> getExtractedSkills() {
        return extractedSkills;
    }

    public void setResumeId(Long resumeId) {
        this.resumeId = resumeId;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setExtractedSkills(List<String> extractedSkills) {
        this.extractedSkills = extractedSkills;
    }
}