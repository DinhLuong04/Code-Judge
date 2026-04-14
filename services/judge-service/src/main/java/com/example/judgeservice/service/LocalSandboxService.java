package com.example.judgeservice.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

@Service
public class LocalSandboxService {
    public String judge(String code, String input, String expectedOutput) {
        try {
            Files.writeString(Path.of("Main.java"), code);

            // 2. Biên dịch: javac Main.java
            Process compile = new ProcessBuilder("javac", "Main.java").start();
            
            // Tóm lấy luồng lỗi của trình biên dịch (nếu có)
            String compileError = new String(compile.getErrorStream().readAllBytes());

            if (!compile.waitFor(10, TimeUnit.SECONDS) || compile.exitValue() != 0) {
                // Trả về chữ CE kèm theo chi tiết lỗi
                return "COMPILATION_ERROR: " + compileError;
            }

            // 3. Chạy với Input
            Process run = new ProcessBuilder("java", "Main").start();
            run.getOutputStream().write(input.getBytes());
            run.getOutputStream().flush();
            run.getOutputStream().close();

            if (!run.waitFor(2, TimeUnit.SECONDS)) {
                run.destroyForcibly();
                return "TIME_LIMIT_EXCEEDED";
            }

            // Đọc lỗi Runtime (nếu code chạy bị Exception như chia cho 0)
            String runtimeError = new String(run.getErrorStream().readAllBytes());
            if (run.exitValue() != 0) {
                return "RUNTIME_ERROR: " + runtimeError;
            }

            // 4. Lấy kết quả thật và KẾT QUẢ MONG ĐỢI
            String actualOutput = new String(run.getInputStream().readAllBytes());
            
            // 🚀 BƯỚC QUAN TRỌNG: CHUẨN HÓA CHUỖI
            // Thay thế \r\n thành \n TRƯỚC, sau đó mới trim() 2 đầu
            String normalizedActual = actualOutput.replace("\r\n", "\n").trim();
            String normalizedExpected = expectedOutput.replace("\r\n", "\n").trim();
            
            // 5. So sánh 2 chuỗi đã được chuẩn hóa
            if (normalizedActual.equals(normalizedExpected)) {
                return "ACCEPTED";
            } else {
                return "WRONG_ANSWER (Output: \n" + normalizedActual + "\n)";
            }

        } catch (Exception e) {
            return "SYSTEM_ERROR: " + e.getMessage();
        } finally {
            new File("Main.java").delete();
            new File("Main.class").delete();
        }
    }
}