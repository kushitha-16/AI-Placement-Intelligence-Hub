package com.placementhub.dto;

import jakarta.validation.constraints.NotBlank;

public class JobAnalyzeRequest {

    @NotBlank(message = "Placement post text cannot be empty")
    private String postText;

    public JobAnalyzeRequest() {
    }

    public JobAnalyzeRequest(String postText) {
        this.postText = postText;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }
}
