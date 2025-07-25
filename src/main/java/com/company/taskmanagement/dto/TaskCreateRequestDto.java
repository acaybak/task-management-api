package com.company.taskmanagement.dto;

import com.company.taskmanagement.domain.Priority;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record TaskCreateRequestDto(
        @NotBlank(message = "Title is mandatory")
        String title,

        String description,

        @FutureOrPresent(message = "Due date must be in the present or future")
        LocalDate dueDate,

        @NotNull(message = "Priority is mandatory")
        Priority priority
) {
}