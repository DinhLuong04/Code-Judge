package com.example.submissionservice.service.Impl;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.example.submissionservice.config.RabbitMQConfig;
import com.example.submissionservice.dto.SubmissionMessage;
import com.example.submissionservice.entity.Submission;
import com.example.submissionservice.repository.SubmissionRepository;
import com.example.submissionservice.service.SubmissionService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubmissionServiceImpl implements SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final RabbitTemplate rabbitTemplate;

    @Override
    @Transactional
    public Submission createSubmission(String username, Long problemId, String code) {
        Submission submission = new Submission();
        submission.setUsername(username);
        submission.setProblemId(problemId);
        submission.setCode(code);
        submission.setStatus("PENDING"); // Đợi chấm

        Submission savedSubmission = submissionRepository.save(submission);
        log.info("📝 Đã lưu bài nộp ID: {} vào Database (PENDING)", savedSubmission.getId());

        // 2. Đóng gói dữ liệu gửi sang RabbitMQ cho Judge Service
        SubmissionMessage message = new SubmissionMessage();
        message.setSubmissionId(savedSubmission.getId());
        message.setProblemId(problemId);
        message.setCode(code);

        // Gửi tới submission.queue
        rabbitTemplate.convertAndSend(RabbitMQConfig.SUBMISSION_QUEUE, message);
        log.info("🚀 Đã đẩy bài nộp ID: {} lên hàng đợi RabbitMQ", savedSubmission.getId());

        return savedSubmission;
    }

    @Override
    @Transactional
    public void updateSubmissionResult(SubmissionMessage result) {

        submissionRepository.findById(result.getSubmissionId()).ifPresentOrElse(submission -> {

            submission.setStatus(result.getStatus());

            submission.setResultDetail(result.getErrorDetail());

            submissionRepository.save(submission);
            log.info("✅ Cập nhật kết quả thành công cho bài nộp ID: {} -> Trạng thái: {}",
                    submission.getId(), result.getStatus());
        }, () -> {
            log.error("❌ Không tìm thấy bài nộp ID: {} để cập nhật kết quả", result.getSubmissionId());
        });
    }

    @Override
    public Submission getSubmissionById(Long id) {
        return submissionRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy bài nộp"));
    }

}
