package com.placementhub.controller;

import com.placementhub.model.InterviewHistory;
import com.placementhub.repository.InterviewHistoryRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interview-history")
public class InterviewHistoryController {

    private final InterviewHistoryRepository repository;

    public InterviewHistoryController(
            InterviewHistoryRepository repository
    ) {
        this.repository = repository;
    }

    @GetMapping
    public List<InterviewHistory> getHistory() {
        return repository.findAll();
    }
}