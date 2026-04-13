package com.example.submissionservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "submissions")
@Data
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    
    @Column(columnDefinition = "TEXT")
    private String code;

    private String status; // PENDING, ACCEPTED, WRONG_ANSWER, COMPILATION_ERROR, etc.

    @Column(columnDefinition = "TEXT")
    private String resultDetail; // Lưu chi tiết lỗi hoặc output thực tế

    private LocalDateTime createdAt = LocalDateTime.now();
}