package com.placementhub.controller;

import com.placementhub.dto.JobAnalyzeRequest;
import com.placementhub.dto.JobAnalyzeResponse;
import com.placementhub.model.JobPost;
import com.placementhub.service.JobPostService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
public class JobPostController {

    private final JobPostService jobPostService;

    public JobPostController(JobPostService jobPostService) {
        this.jobPostService = jobPostService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<JobAnalyzeResponse> analyzeJobPost(
            @Valid @RequestBody JobAnalyzeRequest request
    ) {
        JobAnalyzeResponse response = jobPostService.analyzeAndSaveJobPost(request.getPostText());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<JobPost>> getAllJobPosts() {
        return ResponseEntity.ok(jobPostService.getAllJobPosts());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJobPost(@PathVariable Long id) {
        jobPostService.deleteJobPost(id);
        return ResponseEntity.ok("Job post deleted successfully");
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<JobPost> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request
    ) {
        String status = request.get("status");
        JobPost updatedJob = jobPostService.updateApplicationStatus(id, status);
        return ResponseEntity.ok(updatedJob);
    }
}