package com.company.taskmanagement.dto;

import com.company.taskmanagement.domain.Priority;
import com.company.taskmanagement.domain.Status;
import java.time.LocalDate;

public record TaskResponseDto(
        Long id,
        String title,
        String description,
        LocalDate dueDate,
        Priority priority,
        Status status
) {
}