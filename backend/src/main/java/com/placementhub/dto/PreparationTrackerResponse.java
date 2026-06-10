package com.placementhub.dto;

import java.util.List;

public class PreparationTrackerResponse {

    private String companyName;
    private List<String> topics;
    private int progress;

    public PreparationTrackerResponse(
            String companyName,
            List<String> topics,
            int progress
    ) {
        this.companyName = companyName;
        this.topics = topics;
        this.progress = progress;
    }

    public String getCompanyName() {
        return companyName;
    }

    public List<String> getTopics() {
        return topics;
    }

    public int getProgress() {
        return progress;
    }
}