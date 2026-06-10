package com.placementhub.service;

import com.placementhub.dto.ResourceResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceService {

    public ResourceResponse getResources(String skill) {

        String lower = skill.toLowerCase();

        List<String> resources;

        if (lower.contains("oops")) {
            resources = List.of(
                    "Study class, object, inheritance, polymorphism, abstraction, and encapsulation.",
                    "Practice 20 Java OOPs interview questions.",
                    "Build a mini Java project using OOPs concepts."
            );
        } else if (lower.contains("aptitude")) {
            resources = List.of(
                    "Practice percentages, ratios, averages, time and work.",
                    "Solve 2 aptitude mock tests every week.",
                    "Revise logical reasoning and verbal ability."
            );
        } else if (lower.contains("sql")) {
            resources = List.of(
                    "Practice SELECT, JOIN, GROUP BY, HAVING, and subqueries.",
                    "Create a small database project with 5 tables.",
                    "Solve 30 SQL interview queries."
            );
        } else if (lower.contains("spring boot")) {
            resources = List.of(
                    "Learn controllers, services, repositories, and REST APIs.",
                    "Build a CRUD application using Spring Boot and MySQL.",
                    "Practice API testing using Postman."
            );
        } else if (lower.contains("machine learning")) {
            resources = List.of(
                    "Learn supervised learning, regression, and classification.",
                    "Build one ML project using Python and scikit-learn.",
                    "Understand model evaluation metrics like accuracy and precision."
            );
        } else if (lower.contains("aws")) {
            resources = List.of(
                    "Learn EC2, S3, IAM, and Lambda basics.",
                    "Deploy one simple project on AWS.",
                    "Understand cloud security and pricing basics."
            );
        } else {
            resources = List.of(
                    "Learn the fundamentals of " + skill + ".",
                    "Prepare 10 interview questions on " + skill + ".",
                    "Build one small project or example using " + skill + "."
            );
        }

        return new ResourceResponse(skill, resources);
    }
}