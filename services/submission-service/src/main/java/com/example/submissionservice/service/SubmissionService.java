package com.example.submissionservice.service;

import com.example.submissionservice.dto.SubmissionMessage;
import com.example.submissionservice.entity.Submission;

public interface SubmissionService {

    Submission createSubmission(String username, Long problemId, String code);
    void updateSubmissionResult(SubmissionMessage result);
    Submission getSubmissionById(Long id);
}
