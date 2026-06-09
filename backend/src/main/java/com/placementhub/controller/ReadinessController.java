package com.placementhub.controller;

import com.placementhub.dto.ReadinessResponse;
import com.placementhub.service.ReadinessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/readiness")
public class ReadinessController {

    private final ReadinessService readinessService;

    public ReadinessController(ReadinessService readinessService) {
        this.readinessService = readinessService;
    }

    @GetMapping("/{resumeId}")
    public ResponseEntity<ReadinessResponse> getReadiness(
            @PathVariable Long resumeId
    ) {
        return ResponseEntity.ok(
                readinessService.analyzeResume(resumeId)
        );
    }
}