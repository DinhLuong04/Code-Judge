package com.example.problemservice.controller;

import com.example.problemservice.dto.requests.ProblemRequest;
import com.example.problemservice.dto.response.ProblemResponse;
import com.example.problemservice.entity.Problem;
import com.example.problemservice.service.ProblemService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/problems")
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService problemService;

    // 1. API Thêm đề bài
    @PostMapping
    public ResponseEntity<Problem> create(@RequestBody ProblemRequest request) {
        return ResponseEntity.ok(problemService.createProblem(request));
    }

    // 2. API Lấy danh sách cho User
    @GetMapping
    public ResponseEntity<List<ProblemResponse>> getAll() {
        return ResponseEntity.ok(problemService.getAllProblems());
    }

    // 3. API Lấy chi tiết cho User
    @GetMapping("/{id}")
    public ResponseEntity<ProblemResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(problemService.getProblemById(id));
    }

    // 4. API Nội bộ cho Judge Service (Lấy full testcases)
    @GetMapping("/internal/{id}/testcases")
    public ResponseEntity<String> getTestcasesInternal(@PathVariable Long id) {
        return ResponseEntity.ok(problemService.getHiddenTestcases(id));
    }
}