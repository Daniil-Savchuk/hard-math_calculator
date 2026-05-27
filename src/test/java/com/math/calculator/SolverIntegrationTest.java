package com.math.calculator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SolverIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnBadRequestWhenFieldsAreEmpty() throws Exception {
        // Given: порожній JSON запит (Test Case 2)
        String emptyJsonRequest = "{}";

        // When & Then: відправляємо запит і очікуємо помилку валідації 400 Bad Request
        mockMvc.perform(post("/api/v1/solver/dual-method")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emptyJsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.objective").exists()) // Перевіряємо, що помилка вказує на поле objective
                .andExpect(jsonPath("$.c_coefficients").exists());
    }

    @Test
    void shouldReturnSuccessWhenJsonIsValid() throws Exception {
        // Given: правильний JSON (Test Case 1)
        String validJsonRequest = """
                {
                  "objective": "max",
                  "c_coefficients": [3, 2],
                  "constraints": [
                    {"coefficients": [1, 2], "sign": "<=", "value": 4},
                    {"coefficients": [2, 1], "sign": "<=", "value": 5}
                  ]
                }
                """;

        // When & Then: очікуємо статус 200 OK і правильну відповідь 8.5
        mockMvc.perform(post("/api/v1/solver/dual-method")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.optimal_value").value(8.0));
    }
}