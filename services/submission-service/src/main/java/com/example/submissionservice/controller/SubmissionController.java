package com.example.submissionservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

import com.example.submissionservice.entity.Submission;
import com.example.submissionservice.service.SubmissionService;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
public class SubmissionController {

    private final RabbitTemplate rabbitTemplate;

    private final SubmissionService submissionService;

    @PostMapping
    public Submission submitCode(@RequestParam String username, @RequestBody String code) {
        // Gọi service xử lý: Lưu DB -> Gửi RabbitMQ
        return submissionService.createSubmission(username, code);
    }

    // Thêm API này để bạn có thể kiểm tra kết quả sau khi chấm
    @GetMapping("/{id}")
    public Submission getSubmission(@PathVariable Long id) {
        return submissionService.getSubmissionById(id); 
        // (Bạn nhớ thêm hàm getById này vào SubmissionService nhé)
    }
}