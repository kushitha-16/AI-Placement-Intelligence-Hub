package com.placementhub.controller;

import com.placementhub.dto.ReadinessResponse;
import com.placementhub.service.ReadinessService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/readiness")
public class ReadinessController {

    private final ReadinessService readinessService;

    public ReadinessController(ReadinessService readinessService) {
        this.readinessService = readinessService;
    }

    @GetMapping("/{resumeId}")
    public ReadinessResponse getReadiness(@PathVariable Long resumeId) {
        return readinessService.calculate(resumeId);
    }
}