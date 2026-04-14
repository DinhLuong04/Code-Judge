package com.example.problemservice.dto.requests;

import java.util.List;

import com.example.problemservice.dto.TestcaseDTO;

import lombok.Data;
@Data
public class ProblemRequest {
    private String title;
    private String description;
    private String difficulty;
    private List<TestcaseDTO> testcases;
}
