package com.placementhub.repository;

import com.placementhub.model.InterviewHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewHistoryRepository
        extends JpaRepository<InterviewHistory, Long> {
}