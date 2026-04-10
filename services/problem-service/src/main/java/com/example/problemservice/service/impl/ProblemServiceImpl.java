package com.example.problemservice.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.problemservice.entity.Problem;
import com.example.problemservice.repository.ProblemRepository;
import com.example.problemservice.service.ProblemService;
import org.springframework.cache.annotation.Cacheable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProblemServiceImpl implements ProblemService {
    private final ProblemRepository problemRepository;

    @Override
    @Cacheable(value = "problems")
    public List<Problem> getAllProblems() {
        log.info("Khong tim thay trong Redis! Dang truy van xuong MySQL...");
        return problemRepository.findAll();
    }

}
