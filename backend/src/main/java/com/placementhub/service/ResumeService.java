package com.placementhub.service;

import com.placementhub.dto.ResumeResponse;
import com.placementhub.model.Resume;
import com.placementhub.repository.ResumeRepository;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;

    private final List<String> skillKeywords = Arrays.asList(
            "Java", "Python", "C++", "SQL", "MySQL", "HTML", "CSS", "JavaScript",
            "React", "Node.js", "Spring Boot", "OOPs", "Git", "GitHub", "DBMS",
            "Data Structures", "Algorithms", "Aptitude", "Communication", "Cloud",
            "AWS", "Azure", "Machine Learning", "Artificial Intelligence", "AI",
            "Cybersecurity", "Linux", "REST API", "MongoDB", "Power BI", "Excel",
            "Deep Learning", "NLP", "RAG", "LLMs", "Firebase", "Bootstrap"
    );

    public ResumeService(ResumeRepository resumeRepository) {
        this.resumeRepository = resumeRepository;
    }

    public ResumeResponse uploadResume(MultipartFile file) throws IOException {
        String resumeText = extractTextFromPdf(file);
        List<String> skills = extractSkills(resumeText);

        Resume resume = new Resume(
                file.getOriginalFilename(),
                resumeText,
                String.join(", ", skills)
        );

        Resume savedResume = resumeRepository.save(resume);

        return new ResumeResponse(
                savedResume.getId(),
                savedResume.getFileName(),
                skills
        );
    }

    public List<Resume> getAllResumes() {
        return resumeRepository.findAll();
    }

    private String extractTextFromPdf(MultipartFile file) throws IOException {
        try (PDDocument document = Loader.loadPDF(file.getBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    private List<String> extractSkills(String text) {
        Set<String> skills = new LinkedHashSet<>();
        String lowerText = text.toLowerCase();

        for (String skill : skillKeywords) {
            if (lowerText.contains(skill.toLowerCase())) {
                skills.add(skill);
            }
        }

        return new ArrayList<>(skills);
    }
}