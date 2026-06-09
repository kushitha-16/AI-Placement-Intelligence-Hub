package com.placementhub.service;

import com.placementhub.dto.MatchResponse;
import com.placementhub.model.JobPost;
import com.placementhub.model.Resume;
import com.placementhub.repository.JobPostRepository;
import com.placementhub.repository.ResumeRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MatchService {

    private final JobPostRepository jobPostRepository;
    private final ResumeRepository resumeRepository;

    public MatchService(JobPostRepository jobPostRepository, ResumeRepository resumeRepository) {
        this.jobPostRepository = jobPostRepository;
        this.resumeRepository = resumeRepository;
    }

    public MatchResponse matchResumeWithJob(Long jobPostId, Long resumeId) {
        JobPost jobPost = jobPostRepository.findById(jobPostId)
                .orElseThrow(() -> new RuntimeException("Job post not found"));

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        List<String> jobSkills = splitSkills(jobPost.getRequiredSkills());
        List<String> resumeSkills = splitSkills(resume.getExtractedSkills());

        List<String> matchedSkills = new ArrayList<>();
        List<String> missingSkills = new ArrayList<>();

        for (String jobSkill : jobSkills) {
            boolean found = resumeSkills.stream()
                    .anyMatch(resumeSkill -> resumeSkill.equalsIgnoreCase(jobSkill));

            if (found) {
                matchedSkills.add(jobSkill);
            } else {
                missingSkills.add(jobSkill);
            }
        }

        double matchScore = 0;

        if (!jobSkills.isEmpty()) {
            matchScore = ((double) matchedSkills.size() / jobSkills.size()) * 100;
        }

        matchScore = Math.round(matchScore * 100.0) / 100.0;

        return new MatchResponse(
                jobPost.getCompanyName(),
                jobPost.getRoleName(),
                resume.getFileName(),
                matchScore,
                matchedSkills,
                missingSkills
        );
    }

    private List<String> splitSkills(String skillsText) {
        if (skillsText == null || skillsText.trim().isEmpty()) {
            return new ArrayList<>();
        }

        return Arrays.stream(skillsText.split(","))
                .map(String::trim)
                .filter(skill -> !skill.isEmpty())
                .distinct()
                .collect(Collectors.toList());
    }
}