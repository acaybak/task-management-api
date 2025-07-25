package com.company.taskmanagement.controller;

import com.company.taskmanagement.dto.TaskCreateRequestDto;
import com.company.taskmanagement.dto.TaskResponseDto;
import com.company.taskmanagement.dto.TaskUpdateRequestDto;
import com.company.taskmanagement.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@Valid @RequestBody TaskCreateRequestDto taskCreateRequest) {
        TaskResponseDto createdTask = taskService.createTask(taskCreateRequest);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<Page<TaskResponseDto>> getAllTasksForCurrentUser(Pageable pageable) {
        Page<TaskResponseDto> tasksPage = taskService.getAllTasksForCurrentUser(pageable);
        return ResponseEntity.ok(tasksPage);
    }


    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponseDto> getTaskByIdForCurrentUser(@PathVariable Long taskId) {
        TaskResponseDto task = taskService.getTaskByIdForCurrentUser(taskId);
        return ResponseEntity.ok(task);
    }


    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponseDto> updateTaskForCurrentUser(
            @PathVariable Long taskId,
            @Valid @RequestBody TaskUpdateRequestDto updateRequest) {
        TaskResponseDto updatedTask = taskService.updateTaskForCurrentUser(taskId, updateRequest);
        return ResponseEntity.ok(updatedTask);
    }


    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTaskForCurrentUser(@PathVariable Long taskId) {
        taskService.deleteTaskForCurrentUser(taskId);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }
}