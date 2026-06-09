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
            if (jobSkill.equalsIgnoreCase("C")) {
                continue;
            }

            boolean found = resumeSkills.stream()
                    .anyMatch(resumeSkill -> resumeSkill.equalsIgnoreCase(jobSkill));

            if (found) {
                matchedSkills.add(jobSkill);
            } else {
                missingSkills.add(jobSkill);
            }
        }

        int totalSkills = matchedSkills.size() + missingSkills.size();
        double matchScore = 0;

        if (totalSkills > 0) {
            matchScore = ((double) matchedSkills.size() / totalSkills) * 100;
        }

        matchScore = Math.round(matchScore * 100.0) / 100.0;

        List<String> studyPlan = generateStudyPlan(missingSkills);

        return new MatchResponse(
                jobPost.getCompanyName(),
                jobPost.getRoleName(),
                resume.getFileName(),
                matchScore,
                matchedSkills,
                missingSkills,
                studyPlan
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

    private List<String> generateStudyPlan(List<String> missingSkills) {
        List<String> plan = new ArrayList<>();
        int day = 1;

        if (missingSkills.isEmpty()) {
            plan.add("You are well matched for this opportunity. Revise your resume projects and attend mock interviews.");
            return plan;
        }

        for (String skill : missingSkills) {
            String lower = skill.toLowerCase();

            if (lower.contains("oops")) {
                plan.add("Day " + day++ + ": Revise OOPs basics: class, object, inheritance, polymorphism, abstraction, encapsulation.");
                plan.add("Day " + day++ + ": Practice Java OOPs interview questions and write small programs.");
            } else if (lower.contains("aptitude")) {
                plan.add("Day " + day++ + ": Practice quantitative aptitude: percentages, ratios, averages, time and work.");
                plan.add("Day " + day++ + ": Practice logical reasoning and take one aptitude mock test.");
            } else if (lower.contains("spring boot")) {
                plan.add("Day " + day++ + ": Learn Spring Boot basics: controllers, services, repositories.");
                plan.add("Day " + day++ + ": Build simple REST APIs using Spring Boot and MySQL.");
            } else if (lower.contains("machine learning")) {
                plan.add("Day " + day++ + ": Learn ML basics: supervised learning, classification, regression.");
                plan.add("Day " + day++ + ": Practice one ML mini project using Python.");
            } else if (lower.contains("deep learning")) {
                plan.add("Day " + day++ + ": Learn neural networks, activation functions, and CNN basics.");
            } else if (lower.contains("nlp")) {
                plan.add("Day " + day++ + ": Learn NLP basics: tokenization, stemming, sentiment analysis.");
            } else if (lower.contains("aws")) {
                plan.add("Day " + day++ + ": Learn AWS basics: EC2, S3, IAM, Lambda.");
            } else if (lower.contains("sql")) {
                plan.add("Day " + day++ + ": Practice SQL joins, group by, subqueries, and constraints.");
            } else {
                plan.add("Day " + day++ + ": Learn basics of " + skill + " and prepare 5 interview questions.");
            }
        }

        plan.add("Final Day: Take one mock interview and revise resume projects related to this role.");

        return plan;
    }
}