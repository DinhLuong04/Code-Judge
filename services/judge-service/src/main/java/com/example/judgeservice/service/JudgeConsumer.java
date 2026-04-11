package com.example.judgeservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class JudgeConsumer {

    private final LocalSandboxService localSandboxService;

    @RabbitListener(queues = "submission.queue")
    public void receiveSubmission(String message) {
        log.info("📥 JUDGE SERVICE ĐÃ NHẬN BÀI THI TỪ CLOUD!");
        
        try {
            int startIndex = message.indexOf("code: ");
            if (startIndex == -1) return;
            String code = message.substring(startIndex + 6);
            
           
            
            String input = "5 7\n";               
            String expectedOutput = "12";        
            
            log.info("⏳ Đang chấm bài bằng Local Sandbox...");
            log.info("Input testcase: {}", input.trim());
            log.info("Expected Output: {}", expectedOutput);

            
            String result = localSandboxService.judge(code, input, expectedOutput);
            
            log.info("🏆 KẾT QUẢ CUỐI CÙNG: {}", result);

        } catch (Exception e) {
            log.error("❌ Lỗi hệ thống: {}", e.getMessage());
        }
        log.info("=================================================");
    }
}