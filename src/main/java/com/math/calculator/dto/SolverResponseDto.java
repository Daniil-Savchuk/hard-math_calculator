package com.math.calculator.dto;

import java.util.Map;
import java.util.List;

// Відповідь сервера повертатиме статус, оптимальне значення та змінні
public record SolverResponseDto(
        String status,
        Double optimal_value,
        Map<String, Double> solution_variables,
        List<Object> iterations // Поки що Object для спрощення відображення матриць
) {}
