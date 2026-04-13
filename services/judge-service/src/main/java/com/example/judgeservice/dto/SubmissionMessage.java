package com.example.judgeservice.dto; // Có thể đổi theo package của bạn

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionMessage implements Serializable {
    private Long submissionId;
    private String code;
    private String status;
    private String errorDetail;
}