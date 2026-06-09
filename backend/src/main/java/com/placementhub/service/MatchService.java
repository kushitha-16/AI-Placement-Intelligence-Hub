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
        List<String> resumeSuggestions = generateResumeSuggestions(missingSkills, matchScore, resumeSkills, jobPost);

        return new MatchResponse(
                jobPost.getCompanyName(),
                jobPost.getRoleName(),
                resume.getFileName(),
                matchScore,
                matchedSkills,
                missingSkills,
                studyPlan,
                resumeSuggestions
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

    private List<String> generateResumeSuggestions(List<String> missingSkills,
                                                   double matchScore,
                                                   List<String> resumeSkills,
                                                   JobPost jobPost) {
        List<String> suggestions = new ArrayList<>();

        if (matchScore >= 80) {
            suggestions.add("Your resume is strongly aligned with this opportunity. Focus on revising your projects and interview explanation.");
        } else if (matchScore >= 60) {
            suggestions.add("Your resume has a decent match. Add 1 or 2 missing role-specific skills after learning them.");
        } else {
            suggestions.add("Your resume needs improvement for this role. Add missing technical skills and stronger project keywords.");
        }

        for (String skill : missingSkills) {
            String lower = skill.toLowerCase();

            if (lower.contains("oops")) {
                suggestions.add("Add OOPs concepts under Technical Skills and mention Java OOPs usage in one project.");
            } else if (lower.contains("aptitude")) {
                suggestions.add("Aptitude is usually not added as a resume skill, but prepare it well for the first assessment round.");
            } else if (lower.contains("spring boot")) {
                suggestions.add("Build a small Spring Boot REST API project and add it under Projects.");
            } else if (lower.contains("aws")) {
                suggestions.add("Add cloud basics only after learning AWS EC2, S3, and IAM. Mention any deployment experience.");
            } else if (lower.contains("machine learning")) {
                suggestions.add("Add a Machine Learning mini project with dataset, model, accuracy, and tools used.");
            } else if (lower.contains("deep learning")) {
                suggestions.add("Add Deep Learning only if you have worked on neural networks, CNN, or model training.");
            } else if (lower.contains("nlp")) {
                suggestions.add("Add an NLP project such as sentiment analysis, chatbot, or text summarizer.");
            } else if (lower.contains("sql")) {
                suggestions.add("Mention SQL queries, joins, database design, or DBMS usage in your project description.");
            } else {
                suggestions.add("After learning " + skill + ", add it naturally under Skills or Projects instead of just listing it.");
            }
        }

        if (!resumeSkills.contains("Spring Boot") && jobPost.getRoleName().toLowerCase().contains("programmer")) {
            suggestions.add("For software developer roles, adding Spring Boot or REST API project experience will improve your profile.");
        }

        suggestions.add("Improve project descriptions using this format: Problem → Tech Stack → Your Role → Impact/Result.");
        suggestions.add("Keep resume to one page and use keywords from the job post honestly.");

        return suggestions;
    }
}