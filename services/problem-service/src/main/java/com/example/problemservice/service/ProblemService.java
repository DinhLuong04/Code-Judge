package com.example.problemservice.service;

import java.util.List;

import com.example.problemservice.dto.requests.ProblemRequest;
import com.example.problemservice.dto.response.ProblemResponse;
import com.example.problemservice.entity.Problem;

public interface ProblemService {
    Problem createProblem(ProblemRequest request);
    List<ProblemResponse> getAllProblems();
    ProblemResponse getProblemById(Long id);
    String getHiddenTestcases(Long id);
} 