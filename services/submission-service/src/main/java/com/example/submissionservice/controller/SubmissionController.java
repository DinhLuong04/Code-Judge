package com.example.submissionservice.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import com.example.submissionservice.entity.Submission;
import com.example.submissionservice.service.SubmissionService;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
public class SubmissionController {

    private final SubmissionService submissionService;

    @PostMapping
    public Submission submitCode(@RequestHeader("X-Username") String username, @RequestParam Long problemId,
            @RequestBody String code) {

        return submissionService.createSubmission(username, problemId, code);
    }

    @GetMapping("/{id}")
    public Submission getSubmission(@PathVariable Long id) {
        return submissionService.getSubmissionById(id);

    }
}