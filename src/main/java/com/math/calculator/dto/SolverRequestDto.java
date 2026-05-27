package com.math.calculator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record SolverRequestDto(
        @NotBlank(message = "Цільова функція (objective) не може бути порожньою")
        String objective,

        @NotEmpty(message = "Коефіцієнти функції обов'язкові")
        List<Double> c_coefficients,

        @NotEmpty(message = "Обмеження обов'язкові")
        List<ConstraintDto> constraints
) {}
