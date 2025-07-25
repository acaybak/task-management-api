package com.company.taskmanagement.dto;

import com.company.taskmanagement.domain.Priority;
import com.company.taskmanagement.domain.Status;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record TaskUpdateRequestDto(
        @NotBlank(message = "Title is mandatory")
        String title,
        String description,
        @FutureOrPresent(message = "Due date must be in the present or future")
        LocalDate dueDate,
        @NotNull(message = "Priority is mandatory")
        Priority priority,
        @NotNull(message = "Status is mandatory")
        Status status
) {
}