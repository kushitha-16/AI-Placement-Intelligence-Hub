package com.placementhub.service;

import com.placementhub.dto.ReadinessResponse;
import com.placementhub.model.Resume;
import com.placementhub.repository.ResumeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReadinessService {

    private final ResumeRepository resumeRepository;

    public ReadinessService(ResumeRepository resumeRepository) {
        this.resumeRepository = resumeRepository;
    }

    public ReadinessResponse analyzeResume(Long resumeId) {

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        String skills = resume.getExtractedSkills();

        int skillCount = skills.split(",").length;

        double technicalScore = Math.min(skillCount * 5, 100);

        double projectScore = 80;

        double resumeScore = 85;

        double overallScore =
                (technicalScore * 0.5)
                        + (projectScore * 0.25)
                        + (resumeScore * 0.25);

        String level;

        if (overallScore >= 80) {
            level = "Excellent";
        } else if (overallScore >= 65) {
            level = "Placement Ready";
        } else {
            level = "Needs Improvement";
        }

        List<String> recommendations = new ArrayList<>();

        if (technicalScore < 80) {
            recommendations.add("Improve technical skills and add more technologies.");
        }

        recommendations.add("Continue solving DSA problems.");
        recommendations.add("Practice aptitude and reasoning.");
        recommendations.add("Prepare project explanations.");
        recommendations.add("Attend mock interviews.");

        return new ReadinessResponse(
                technicalScore,
                projectScore,
                resumeScore,
                Math.round(overallScore * 100.0) / 100.0,
                level,
                recommendations
        );
    }
}