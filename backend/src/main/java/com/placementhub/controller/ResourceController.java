package com.placementhub.controller;

import com.placementhub.dto.ResourceResponse;
import com.placementhub.service.ResourceService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resources")
public class ResourceController {

    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping("/{skill}")
    public ResourceResponse getResources(@PathVariable String skill) {
        return resourceService.getResources(skill);
    }
}