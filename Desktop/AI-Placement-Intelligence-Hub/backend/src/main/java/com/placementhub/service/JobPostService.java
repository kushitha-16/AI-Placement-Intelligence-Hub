package com.placementhub.service;

import com.placementhub.dto.JobAnalyzeResponse;
import com.placementhub.model.JobPost;
import com.placementhub.repository.JobPostRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class JobPostService {

    private final JobPostRepository jobPostRepository;

    private final List<String> skillKeywords = Arrays.asList(
            "Java", "Python", "C++", "SQL", "MySQL", "HTML", "CSS", "JavaScript",
            "React", "Node.js", "Spring Boot", "OOPs", "Git", "GitHub", "DBMS",
            "Data Structures", "Algorithms", "Aptitude", "Communication", "Cloud",
            "AWS", "Azure", "Machine Learning", "Artificial Intelligence", "AI",
            "Cybersecurity", "Linux", "REST API", "MongoDB", "Power BI", "Excel",
            "Deep Learning", "NLP", "RL", "Causal Inference", "AI Agents", "RAG", "LLMs"
    );

    public JobPostService(JobPostRepository jobPostRepository) {
        this.jobPostRepository = jobPostRepository;
    }

    public JobAnalyzeResponse analyzeAndSaveJobPost(String postText) {
        String cleanedText = postText.trim();

        String companyName = extractCompanyName(cleanedText);
        String roleName = extractRoleName(cleanedText);
        String eligibility = extractEligibility(cleanedText);
        List<String> requiredSkills = extractSkills(cleanedText);
        String selectionRounds = extractSelectionRounds(cleanedText);
        String deadline = extractDeadline(cleanedText);
        String applyLink = extractApplyLink(cleanedText);

        JobPost jobPost = new JobPost(
                companyName,
                roleName,
                eligibility,
                String.join(", ", requiredSkills),
                selectionRounds,
                deadline,
                applyLink,
                cleanedText
        );

        JobPost savedJobPost = jobPostRepository.save(jobPost);

        return new JobAnalyzeResponse(
                savedJobPost.getId(),
                companyName,
                roleName,
                eligibility,
                requiredSkills,
                selectionRounds,
                deadline,
                applyLink,
                cleanedText
        );
    }

    public List<JobPost> getAllJobPosts() {
        return jobPostRepository.findAll();
    }
    public void deleteJobPost(Long id) {
    jobPostRepository.deleteById(id);
}

    private String extractCompanyName(String text) {
        String lowerText = text.toLowerCase();

        if (lowerText.contains("amazon")) {
            return "Amazon India";
        }
        if (lowerText.contains("cognizant")) {
            return "Cognizant";
        }
        if (lowerText.contains("tcs")) {
            return "TCS";
        }
        if (lowerText.contains("infosys")) {
            return "Infosys";
        }
        if (lowerText.contains("wipro")) {
            return "Wipro";
        }
        if (lowerText.contains("accenture")) {
            return "Accenture";
        }
        if (lowerText.contains("capgemini")) {
            return "Capgemini";
        }
        if (lowerText.contains("deloitte")) {
            return "Deloitte";
        }

        String companyPattern = "(?i)(company|organization|organisation)\\s*[:\\-]\\s*(.+)";
        String matched = findByPattern(text, companyPattern);

        if (!matched.equals("Not found")) {
            return matched;
        }

        String[] lines = text.split("\\r?\\n");
        if (lines.length > 0) {
            return lines[0]
                    .replace("Greetings from", "")
                    .replace("!", "")
                    .trim();
        }

        return "Not found";
    }

    private String extractRoleName(String text) {
        String lowerText = text.toLowerCase();

        if (lowerText.contains("amazon ml summer school")) {
            return "Amazon ML Summer School 2026";
        }

        String rolePattern = "(?i)(role|position|designation|job title|program)\\s*[:\\-]\\s*(.+)";
        return findByPattern(text, rolePattern);
    }

    private String extractEligibility(String text) {
        String[] lines = text.split("\\r?\\n");
        StringBuilder eligibility = new StringBuilder();
        boolean capture = false;

        for (String line : lines) {
            String lower = line.toLowerCase();

            if (lower.contains("who should apply") || lower.contains("eligibility")) {
                capture = true;
                continue;
            }

            if (capture) {
                if (lower.contains("program details") ||
                        lower.contains("selection") ||
                        lower.contains("why it matters") ||
                        lower.contains("request")) {
                    break;
                }

                if (!line.trim().isEmpty()) {
                    eligibility.append(line.trim()).append(" ");
                }
            }
        }

        if (!eligibility.toString().trim().isEmpty()) {
            return eligibility.toString().trim();
        }

        for (String line : lines) {
            String lower = line.toLowerCase();

            if (lower.contains("eligible") ||
                    lower.contains("cgpa") ||
                    lower.contains("percentage") ||
                    lower.contains("backlogs")) {
                return removeLabel(line);
            }
        }

        return "Not found";
    }

    private List<String> extractSkills(String text) {
        Set<String> skills = new LinkedHashSet<>();
        String lowerText = text.toLowerCase();

        for (String skill : skillKeywords) {
            if (skill.equals("C")) {
                continue;
            }

            if (lowerText.contains(skill.toLowerCase())) {
                skills.add(skill);
            }
        }

        return new ArrayList<>(skills);
    }

    private String extractSelectionRounds(String text) {
        String[] lines = text.split("\\r?\\n");

        for (String line : lines) {
            String lower = line.toLowerCase();

            if (lower.contains("selection") ||
                    lower.contains("round") ||
                    lower.contains("assessment") ||
                    lower.contains("interview")) {
                return removeLabel(line);
            }
        }

        return "Not found";
    }

    private String extractDeadline(String text) {
        String[] lines = text.split("\\r?\\n");

        for (String line : lines) {
            String lower = line.toLowerCase();

            if (lower.contains("last date") ||
                    lower.contains("deadline") ||
                    lower.contains("apply before") ||
                    lower.contains("registration closes") ||
                    lower.contains("last date to register")) {
                return removeLabel(line);
            }
        }

        return "Not found";
    }

    private String extractApplyLink(String text) {
        Pattern pattern = Pattern.compile("(https?://\\S+)");
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return "";
    }

    private String findByPattern(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return matcher.group(2).trim();
        }

        return "Not found";
    }

    private String removeLabel(String line) {
        return line.replaceFirst("(?i)^[a-zA-Z ]+\\s*[:\\-]\\s*", "").trim();
    }
}