package com.company.taskmanagement.service;

import com.company.taskmanagement.dto.TaskCreateRequestDto;
import com.company.taskmanagement.dto.TaskResponseDto;
import com.company.taskmanagement.dto.TaskUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {


    TaskResponseDto createTask(TaskCreateRequestDto taskCreateRequest);

    Page<TaskResponseDto> getAllTasksForCurrentUser(Pageable pageable);


    TaskResponseDto getTaskByIdForCurrentUser(Long taskId);


    TaskResponseDto updateTaskForCurrentUser(Long taskId, TaskUpdateRequestDto updateRequest);


    void deleteTaskForCurrentUser(Long taskId);
}