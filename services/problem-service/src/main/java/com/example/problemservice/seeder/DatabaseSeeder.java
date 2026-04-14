package com.example.problemservice.seeder;

import com.example.problemservice.dto.requests.ProblemRequest;
import com.example.problemservice.repository.ProblemRepository;
import com.example.problemservice.service.ProblemService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseSeeder implements CommandLineRunner {

    private final ProblemRepository problemRepository;
    private final ProblemService problemService; // Gọi thẳng qua Service để tận dụng logic
    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception {
        // Chỉ nạp dữ liệu nếu bảng problems đang trống
        if (problemRepository.count() == 0) {
            log.info("🌱 Đang khởi tạo dữ liệu đề bài từ file JSON...");

            try {
                // Đọc file problems.json từ thư mục resources
                InputStream inputStream = new ClassPathResource("problems.json").getInputStream();
                
                // Ép kiểu JSON thành List các đối tượng ProblemRequest
                List<ProblemRequest> problems = objectMapper.readValue(inputStream, new TypeReference<>() {});

                // Vòng lặp lưu từng bài vào Database
                for (ProblemRequest request : problems) {
                    problemService.createProblem(request);
                }

                log.info("✅ Đã nạp thành công {} bài tập vào Database!", problems.size());

            } catch (Exception e) {
                log.error("❌ Lỗi khi đọc file problems.json: {}", e.getMessage());
            }

        } else {
            log.info("⚡ Database đã có dữ liệu, bỏ qua bước Seed.");
        }
    }
}