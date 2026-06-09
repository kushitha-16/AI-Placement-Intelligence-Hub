package com.placementhub.controller;

import com.placementhub.dto.InterviewEvaluationRequest;
import com.placementhub.dto.InterviewEvaluationResponse;
import com.placementhub.service.MockInterviewService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mock-interview")
public class MockInterviewController {

    private final MockInterviewService service;

    public MockInterviewController(MockInterviewService service) {
        this.service = service;
    }

    @PostMapping("/evaluate")
    public InterviewEvaluationResponse evaluate(
            @RequestBody InterviewEvaluationRequest request
    ) {
        return service.evaluate(request.getAnswer());
    }
}