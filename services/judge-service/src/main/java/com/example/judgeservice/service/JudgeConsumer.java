package com.example.judgeservice.service;

import com.example.judgeservice.config.RabbitMQConfig;
import com.example.judgeservice.dto.SubmissionMessage;
import com.example.judgeservice.dto.TestcaseDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class JudgeConsumer {

    private final LocalSandboxService localSandboxService;
    private final RabbitTemplate rabbitTemplate;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    
    private final String PROBLEM_SERVICE_URL = "http://problem-service/api/problems/internal/";

    @RabbitListener(queues = RabbitMQConfig.SUBMISSION_QUEUE)
    public void receiveSubmission(SubmissionMessage message) {
        log.info("📥 JUDGE ĐANG XỬ LÝ BÀI THI ID: {} CHO PROBLEM ID: {}", 
                 message.getSubmissionId(), message.getProblemId());

        try {
            // 1. Gọi API lấy Testcase ẩn từ Problem Service
            String url = PROBLEM_SERVICE_URL + message.getProblemId() + "/testcases";
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            String testcasesJson = response.getBody();

            // 2. Chuyển chuỗi JSON thành List<TestcaseDTO>
            List<TestcaseDTO> testcases = objectMapper.readValue(
                    testcasesJson, new TypeReference<List<TestcaseDTO>>() {}
            );

            log.info("🔍 Đã tải {} testcases. Bắt đầu chấm...", testcases.size());

            boolean isAccepted = true;
            String finalStatus = "ACCEPTED";
            String detail = "Vượt qua toàn bộ testcase!";

            // 3. Vòng lặp chấm từng Testcase
            for (int i = 0; i < testcases.size(); i++) {
                TestcaseDTO tc = testcases.get(i);
                
                String result = localSandboxService.judge(message.getCode(), tc.getInput(), tc.getExpectedOutput());

                if (!result.equals("ACCEPTED")) {
                    isAccepted = false;
                    finalStatus = result; // Có thể là WRONG_ANSWER, TIME_LIMIT_EXCEEDED, CE...
                    detail = "Lỗi tại Testcase #" + (i + 1) + ". Result: " + result;
                    log.warn("❌ {}", detail);
                    break; // Sai 1 testcase là dừng luôn, không chấm tiếp
                }
            }

            // 4. Cập nhật kết quả vào Message
            message.setStatus(finalStatus);
            message.setErrorDetail(detail);

            // 5. Gửi ngược về Submission Service
            rabbitTemplate.convertAndSend(RabbitMQConfig.RESULT_QUEUE, message);
            log.info("✅ Đã chốt kết quả bài {} là: {}", message.getSubmissionId(), finalStatus);

        } catch (Exception e) {
            log.error("❌ Lỗi hệ thống nghiêm trọng khi chấm bài {}: {}", message.getSubmissionId(), e.getMessage());
            message.setStatus("SYSTEM_ERROR");
            message.setErrorDetail(e.getMessage());
            rabbitTemplate.convertAndSend(RabbitMQConfig.RESULT_QUEUE, message);
        }
    }
}