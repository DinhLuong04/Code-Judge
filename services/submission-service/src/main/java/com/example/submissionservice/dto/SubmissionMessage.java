package com.example.submissionservice.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionMessage implements Serializable {
    private Long submissionId;
    private String code;
    private String status;
    private String errorDetail;
}