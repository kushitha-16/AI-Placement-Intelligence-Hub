package com.placementhub.service;

import com.placementhub.dto.ReadinessResponse;
import com.placementhub.model.Resume;
import com.placementhub.repository.ResumeRepository;
import org.springframework.stereotype.Service;

@Service
public class ReadinessService {

    private final ResumeRepository resumeRepository;

    public ReadinessService(ResumeRepository resumeRepository) {
        this.resumeRepository = resumeRepository;
    }

    public ReadinessResponse calculate(Long resumeId) {

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        String skills = resume.getExtractedSkills();
        String resumeText = resume.getResumeText().toLowerCase();

        int technicalScore = 50;
        int projectScore = 50;
        int communicationScore = 70;

        if (skills.contains("Java")) technicalScore += 10;
        if (skills.contains("Python")) technicalScore += 10;
        if (skills.contains("SQL")) technicalScore += 10;
        if (skills.contains("Git")) technicalScore += 5;

        if (resumeText.contains("project")) {
            projectScore += 30;
        }

        if (resumeText.contains("communication")) {
            communicationScore += 10;
        }

        int overallScore = (technicalScore + projectScore + communicationScore) / 3;

        String status;

        if (overallScore >= 80) {
            status = "Ready for Placements";
        } else if (overallScore >= 60) {
            status = "Needs Improvement";
        } else {
            status = "High Preparation Required";
        }

        return new ReadinessResponse(
                technicalScore,
                projectScore,
                communicationScore,
                overallScore,
                status
        );
    }
}