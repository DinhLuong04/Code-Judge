package com.example.submissionservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
public class SubmissionController {

    private final RabbitTemplate rabbitTemplate;

    @PostMapping
    public String submitCode(@RequestParam String username, @RequestParam String code) {
        
        // 1. Giả lập việc tạo ra một thông báo nộp bài
        String message = "Thí sinh [" + username + "] vừa nộp đoạn code: " + code;

        // 2. NÉM THẲNG LÊN CLOUD (Vào đúng cái hàng đợi submission.queue)
        rabbitTemplate.convertAndSend("submission.queue", message);

        // 3. Trả về ngay lập tức cho người dùng (Không bắt họ chờ chấm điểm)
        return "Hệ thống đã nhận bài của " + username + ". Vui lòng đi uống nước, lát quay lại xem điểm!";
    }
}