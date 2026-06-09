package com.placementhub.controller;

import com.placementhub.dto.InterviewResponse;
import com.placementhub.service.InterviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interview")
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @GetMapping
    public ResponseEntity<InterviewResponse> generateQuestions(
            @RequestParam Long jobPostId,
            @RequestParam Long resumeId
    ) {
        return ResponseEntity.ok(
                interviewService.generateQuestions(jobPostId, resumeId)
        );
    }
}