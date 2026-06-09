package com.placementhub.controller;

import com.placementhub.dto.MatchRequest;
import com.placementhub.dto.MatchResponse;
import com.placementhub.service.MatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/match")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping
    public ResponseEntity<MatchResponse> matchResumeWithJob(@RequestBody MatchRequest request) {
        MatchResponse response = matchService.matchResumeWithJob(
                request.getJobPostId(),
                request.getResumeId()
        );

        return ResponseEntity.ok(response);
    }
}