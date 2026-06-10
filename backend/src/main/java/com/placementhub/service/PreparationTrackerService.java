package com.placementhub.service;

import com.placementhub.dto.PreparationTrackerResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PreparationTrackerService {

    public PreparationTrackerResponse getTracker(String company) {

        List<String> topics;

        switch (company.toLowerCase()) {

            case "amazon":
                topics = List.of(
                        "DSA ✅",
                        "OOPs 🔄",
                        "DBMS ⏳",
                        "Operating Systems ⏳",
                        "Computer Networks ⏳",
                        "Aptitude 🔄"
                );
                break;

            case "cognizant":
                topics = List.of(
                        "Java ✅",
                        "SQL ✅",
                        "OOPs 🔄",
                        "Aptitude 🔄",
                        "Communication ✅"
                );
                break;

            default:
                topics = List.of(
                        "DSA 🔄",
                        "OOPs 🔄",
                        "DBMS 🔄"
                );
        }

        return new PreparationTrackerResponse(
                company,
                topics,
                40
        );
    }
}