package com.example.problemservice;

import com.example.problemservice.entity.Problem;
import com.example.problemservice.repository.ProblemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
@EnableCaching
public class ProblemServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProblemServiceApplication.class, args);
    }

    
    @Bean
    public CommandLineRunner initData(ProblemRepository problemRepository) {
        return args -> {
           
            if (problemRepository.count() == 0) {
                problemRepository.saveAll(List.of(
                        Problem.builder()
                                .title("In ra Hello World")
                                .description("Viết chương trình in ra chữ Hello World ra màn hình.")
                                .difficulty("EASY")
                                .hiddenTestcases("Hello World")
                                .build(),
                        Problem.builder()
                                .title("Tính tổng 2 số")
                                .description("Nhập vào A và B, in ra tổng A + B.")
                                .difficulty("EASY")
                                .hiddenTestcases("5 7 | 12")
                                .build(),
                        Problem.builder()
                                .title("Tìm đường đi ngắn nhất")
                                .description("Cho một đồ thị có trọng số, tìm đường đi ngắn nhất từ đỉnh 1 đến đỉnh N.")
                                .difficulty("HARD")
                                .hiddenTestcases("...")
                                .build()
                ));
                System.out.println("====== DỮ LIỆU MẪU ĐÃ ĐƯỢC THÊM VÀO MYSQL ======");
            }
        };
    }
}