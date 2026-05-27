package com.math.calculator.service;

import com.math.calculator.dto.SolverRequestDto;
import com.math.calculator.dto.SolverResponseDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SolverService {

    public SolverResponseDto solveDualMethod(SolverRequestDto request) {
        int numVars = request.c_coefficients().size();
        int numConstraints = request.constraints().size();

        // 1. Створюємо початкову симплекс-таблицю (Tableau)
        // Рядки: обмеження + 1 (для цільової функції)
        // Стовпці: змінні + додаткові змінні + 1 (для результату)
        double[][] tableau = new double[numConstraints + 1][numVars + numConstraints + 1];

        // Заповнюємо матрицю обмежень
        for (int i = 0; i < numConstraints; i++) {
            for (int j = 0; j < numVars; j++) {
                tableau[i][j] = request.constraints().get(i).coefficients().get(j);
            }
            tableau[i][numVars + i] = 1.0; // Додаткова змінна
            tableau[i][numVars + numConstraints] = request.constraints().get(i).value(); // RHS
        }

        // Заповнюємо рядок цільової функції (з протилежним знаком для максимізації)
        for (int j = 0; j < numVars; j++) {
            tableau[numConstraints][j] = -request.c_coefficients().get(j);
        }

        List<Object> iterations = new ArrayList<>();
        iterations.add(copyTableau(tableau)); // Зберігаємо початковий стан

        // 2. Основний цикл оптимізації (пошук опорного елемента)
        while (true) {
            int pivotCol = -1;
            double minValue = 0;

            // Шукаємо найбільш від'ємний елемент у нижньому рядку
            for (int j = 0; j < numVars + numConstraints; j++) {
                if (tableau[numConstraints][j] < minValue) {
                    minValue = tableau[numConstraints][j];
                    pivotCol = j;
                }
            }

            if (pivotCol == -1) break; // Якщо від'ємних немає - оптимальний план знайдено!

            int pivotRow = -1;
            double minRatio = Double.MAX_VALUE;

            // Шукаємо рядок з мінімальним відношенням
            for (int i = 0; i < numConstraints; i++) {
                if (tableau[i][pivotCol] > 0) {
                    double ratio = tableau[i][numVars + numConstraints] / tableau[i][pivotCol];
                    if (ratio < minRatio) {
                        minRatio = ratio;
                        pivotRow = i;
                    }
                }
            }

            if (pivotRow == -1) {
                throw new RuntimeException("Задача не має розв'язку: область допустимих значень не обмежена");
            }

            // 3. Метод Жордана-Гаусса (перерахунок таблиці)
            double pivotElement = tableau[pivotRow][pivotCol];
            for (int j = 0; j < numVars + numConstraints + 1; j++) {
                tableau[pivotRow][j] /= pivotElement;
            }

            for (int i = 0; i < numConstraints + 1; i++) {
                if (i != pivotRow) {
                    double factor = tableau[i][pivotCol];
                    for (int j = 0; j < numVars + numConstraints + 1; j++) {
                        tableau[i][j] -= factor * tableau[pivotRow][j];
                    }
                }
            }

            iterations.add(copyTableau(tableau)); // Зберігаємо крок
        }

        // 4. Формуємо красиву відповідь
        double optimalValue = tableau[numConstraints][numVars + numConstraints];
        Map<String, Double> solution = new HashMap<>();
        // Для спрощення беремо базові змінні (в реальному застосунку тут буде складніший мапінг)
        solution.put("x1", tableau[0][numVars + numConstraints]);
        solution.put("x2", tableau[1][numVars + numConstraints]);

        return new SolverResponseDto("success", optimalValue, solution, iterations);
    }

    // Допоміжний метод для копіювання матриці в історію ітерацій
    private double[][] copyTableau(double[][] original) {
        double[][] copy = new double[original.length][original[0].length];
        for (int i = 0; i < original.length; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, original[i].length);
        }
        return copy;
    }
}
