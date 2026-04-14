package com.example.problemservice.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.problemservice.dto.requests.ProblemRequest;
import com.example.problemservice.dto.response.ProblemResponse;
import com.example.problemservice.entity.Problem;
import com.example.problemservice.repository.ProblemRepository;
import com.example.problemservice.service.ProblemService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProblemServiceImpl implements ProblemService {
    private final ProblemRepository problemRepository;
    private final ObjectMapper objectMapper; // Spring tự động nạp Jackson

    @Override
    @Transactional
    public Problem createProblem(ProblemRequest request) {
        try {
            // Chuyển List Object sang chuỗi JSON để lưu vào DB
            String testcasesJson = objectMapper.writeValueAsString(request.getTestcases());
            
            Problem problem = Problem.builder()
                    .title(request.getTitle())
                    .description(request.getDescription())
                    .difficulty(request.getDifficulty())
                    .hiddenTestcases(testcasesJson)
                    .build();
                    
            return problemRepository.save(problem);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Lỗi chuyển đổi Testcase sang JSON", e);
        }
    }

    @Override
    public List<ProblemResponse> getAllProblems() {
        return problemRepository.findAll().stream()
                .map(p -> ProblemResponse.builder()
                        .id(p.getId())
                        .title(p.getTitle())
                        .description(p.getDescription())
                        .difficulty(p.getDifficulty())
                        .build())
                .toList();
    }

    @Override
    public ProblemResponse getProblemById(Long id) {
        Problem p = problemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài tập"));
        return ProblemResponse.builder()
                .id(p.getId())
                .title(p.getTitle())
                .description(p.getDescription())
                .difficulty(p.getDifficulty())
                .build();
    }

    @Override
    public String getHiddenTestcases(Long id) {
        Problem p = problemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài tập"));
        return p.getHiddenTestcases(); // Trả về chuỗi JSON thô cho Judge Service
    }

}
