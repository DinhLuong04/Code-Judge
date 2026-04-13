package com.example.judgeservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.example.judgeservice.config.RabbitMQConfig;
import com.example.judgeservice.dto.SubmissionMessage;

@Service
@Slf4j
@RequiredArgsConstructor
public class JudgeConsumer {

    private final LocalSandboxService localSandboxService;
    private final RabbitTemplate rabbitTemplate; 

    @RabbitListener(queues = RabbitMQConfig.SUBMISSION_QUEUE)
    public void receiveSubmission(SubmissionMessage message) {
        log.info("📥 ĐÃ NHẬN BÀI THI ID: {}", message.getSubmissionId());
        
        try {
            // Giả lập đề bài: Cộng 2 số (Sau này sẽ lấy từ DB)
            String input = "10 20\n";
            String expectedOutput = "30";

        
            String result = localSandboxService.judge(message.getCode(), input, expectedOutput);
            
          
            message.setStatus(result);
            message.setErrorDetail(result);

            
            rabbitTemplate.convertAndSend(RabbitMQConfig.RESULT_QUEUE, message);
            
            log.info("✅ Đã gửi kết quả bài {} về Cloud: {}", message.getSubmissionId(), result);

        } catch (Exception e) {
            log.error("❌ Lỗi xử lý bài {}: {}", message.getSubmissionId(), e.getMessage());
        }
        log.info("=================================================");
    }
}