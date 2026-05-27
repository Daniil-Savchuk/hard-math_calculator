package com.math.calculator.controller;

import com.math.calculator.dto.SolverRequestDto;
import com.math.calculator.dto.SolverResponseDto;
import com.math.calculator.service.SolverService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/solver")
public class SolverController {

    private final SolverService solverService;

    // Constructor Injection (вимога з завдання)
    public SolverController(SolverService solverService) {
        this.solverService = solverService;
    }

    @PostMapping("/dual-method")
    public SolverResponseDto solve(
            @Valid @RequestBody SolverRequestDto request // @Valid запускає перевірку даних
    ) {
        return solverService.solveDualMethod(request);
    }
}