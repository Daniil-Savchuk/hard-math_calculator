package com.math.calculator;

import com.math.calculator.dto.ConstraintDto;
import com.math.calculator.dto.SolverRequestDto;
import com.math.calculator.dto.SolverResponseDto;
import com.math.calculator.service.SolverService;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class SolverServiceTest {

    private final SolverService solverService = new SolverService();

    @Test
    void testSuccessfulOptimizationCalculation() {
        // Given: налаштовуємо тестові дані (як у README)
        List<Double> c = List.of(3.0, 2.0);
        List<ConstraintDto> constraints = List.of(
                new ConstraintDto(List.of(1.0, 2.0), "<=", 4.0),
                new ConstraintDto(List.of(2.0, 1.0), "<=", 5.0)
        );
        SolverRequestDto request = new SolverRequestDto("max", c, constraints);

        // When: викликаємо метод
        SolverResponseDto response = solverService.solveDualMethod(request);

        // Then: перевіряємо результати
        assertEquals("success", response.status());
        assertEquals(8.0, response.optimal_value(), 0.001); // 8.0 - правильна відповідь
        assertNotNull(response.iterations());
        assertTrue(response.iterations().size() > 1); // Має бути кілька ітерацій
    }
}
