package com.placementhub.service;

import com.placementhub.dto.InterviewResponse;
import com.placementhub.model.JobPost;
import com.placementhub.model.Resume;
import com.placementhub.repository.JobPostRepository;
import com.placementhub.repository.ResumeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class InterviewService {

    private final JobPostRepository jobPostRepository;
    private final ResumeRepository resumeRepository;

    public InterviewService(JobPostRepository jobPostRepository, ResumeRepository resumeRepository) {
        this.jobPostRepository = jobPostRepository;
        this.resumeRepository = resumeRepository;
    }

    public InterviewResponse generateQuestions(Long jobPostId, Long resumeId) {
        JobPost jobPost = jobPostRepository.findById(jobPostId)
                .orElseThrow(() -> new RuntimeException("Job post not found"));

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        List<String> resumeSkills = splitSkills(resume.getExtractedSkills());

        List<String> technicalQuestions = new ArrayList<>();
        List<String> projectQuestions = new ArrayList<>();

        for (String skill : resumeSkills) {
            String lower = skill.toLowerCase();

            if (lower.contains("java")) {
                technicalQuestions.add("Explain OOPs concepts in Java with examples.");
                technicalQuestions.add("What is the difference between method overloading and method overriding?");
            } else if (lower.contains("sql")) {
                technicalQuestions.add("Explain joins in SQL with examples.");
                technicalQuestions.add("What is the difference between primary key and foreign key?");
            } else if (lower.contains("python")) {
                technicalQuestions.add("What are lists, tuples, and dictionaries in Python?");
            } else if (lower.contains("html") || lower.contains("css")) {
                technicalQuestions.add("Explain the difference between HTML and CSS.");
                technicalQuestions.add("How do you make a webpage responsive?");
            } else if (lower.contains("git")) {
                technicalQuestions.add("What is the difference between git pull and git fetch?");
            } else if (lower.contains("firebase")) {
                technicalQuestions.add("How did you use Firebase in your project?");
            } else if (lower.contains("ai")) {
                technicalQuestions.add("What is Artificial Intelligence and how is it used in your project?");
            }
        }

        technicalQuestions.add("Explain your strongest technical skill.");
        technicalQuestions.add("How do you debug errors in your code?");

        projectQuestions.add("Explain your best project from problem statement to implementation.");
        projectQuestions.add("What was your role in your project?");
        projectQuestions.add("What challenges did you face and how did you solve them?");
        projectQuestions.add("How did you use database/backend/frontend in your project?");
        projectQuestions.add("What improvements will you add in the future?");

        List<String> hrQuestions = Arrays.asList(
                "Tell me about yourself.",
                "Why should we hire you?",
                "What are your strengths and weaknesses?",
                "Why do you want to join " + jobPost.getCompanyName() + "?",
                "Where do you see yourself in 5 years?",
                "Are you comfortable working in a team?",
                "Tell me about a time you solved a difficult problem."
        );

        return new InterviewResponse(
                jobPost.getCompanyName(),
                jobPost.getRoleName(),
                technicalQuestions,
                hrQuestions,
                projectQuestions
        );
    }

    private List<String> splitSkills(String skillsText) {
        if (skillsText == null || skillsText.trim().isEmpty()) {
            return new ArrayList<>();
        }

        return Arrays.stream(skillsText.split(","))
                .map(String::trim)
                .filter(skill -> !skill.isEmpty())
                .toList();
    }
}