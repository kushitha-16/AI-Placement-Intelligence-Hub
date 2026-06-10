package com.placementhub.controller;

import com.placementhub.dto.PreparationTrackerResponse;
import com.placementhub.service.PreparationTrackerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/preparation")
public class PreparationTrackerController {

    private final PreparationTrackerService service;

    public PreparationTrackerController(
            PreparationTrackerService service
    ) {
        this.service = service;
    }

    @GetMapping("/{company}")
    public PreparationTrackerResponse getTracker(
            @PathVariable String company
    ) {
        return service.getTracker(company);
    }
}