package com.example.submissionservice.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.example.submissionservice.config.RabbitMQConfig;
import com.example.submissionservice.dto.SubmissionMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResultConsumer {
    private final SubmissionService submissionService;
    @RabbitListener(queues = RabbitMQConfig.RESULT_QUEUE)
    public void receiveResult(SubmissionMessage message) {
        log.info("📥 ĐÃ NHẬN KẾT QUẢ BÀI THI ID: {} - KẾT QUẢ: {}", message.getSubmissionId(), message.getStatus());
        
        try {
            submissionService.updateSubmissionResult(message);
            log.info("✅ Đã cập nhật kết quả bài {} vào DB", message.getSubmissionId());
        } catch (Exception e) {
            log.error("❌ Lỗi cập nhật kết quả bài {}: {}", message.getSubmissionId(), e.getMessage());
        }
        log.info("=================================================");
    }
}
