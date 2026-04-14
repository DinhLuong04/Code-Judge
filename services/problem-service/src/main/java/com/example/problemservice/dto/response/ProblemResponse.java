package com.example.problemservice.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProblemResponse {
    private Long id;
    private String title;
    private String description;
    private String difficulty;
}
