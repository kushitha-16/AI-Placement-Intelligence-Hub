package com.placementhub.dto;

import java.util.List;

public class ResourceResponse {

    private String skill;
    private List<String> resources;

    public ResourceResponse(String skill, List<String> resources) {
        this.skill = skill;
        this.resources = resources;
    }

    public String getSkill() {
        return skill;
    }

    public List<String> getResources() {
        return resources;
    }
}