package com.company.taskmanagement.service.impl;

import com.company.taskmanagement.domain.Status;
import com.company.taskmanagement.domain.Task;
import com.company.taskmanagement.domain.User;
import com.company.taskmanagement.dto.TaskCreateRequestDto;
import com.company.taskmanagement.dto.TaskResponseDto;
import com.company.taskmanagement.dto.TaskUpdateRequestDto;
import com.company.taskmanagement.exception.TaskNotFoundException;
import com.company.taskmanagement.exception.UnauthorizedException;
import com.company.taskmanagement.repository.TaskRepository;
import com.company.taskmanagement.repository.UserRepository;
import com.company.taskmanagement.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    public TaskResponseDto createTask(TaskCreateRequestDto request) {
        User currentUser = getCurrentUser();
        log.info("User '{}' is creating a new task with title: '{}'", currentUser.getEmail(), request.title());

        Task newTask = new Task();
        newTask.setTitle(request.title());
        newTask.setDescription(request.description());
        newTask.setDueDate(request.dueDate());
        newTask.setPriority(request.priority());
        newTask.setStatus(Status.TO_DO);
        newTask.setUser(currentUser);

        Task savedTask = taskRepository.save(newTask);
        log.info("Task created successfully with ID: {} for user: {}", savedTask.getId(), currentUser.getEmail());

        return convertToDto(savedTask);
    }

    @Override
    public Page<TaskResponseDto> getAllTasksForCurrentUser(Pageable pageable) {
        User currentUser = getCurrentUser();
        log.info("Fetching all tasks for user: {}", currentUser.getEmail());
        Page<Task> tasksPage = taskRepository.findByUserId(currentUser.getId(), pageable);
        return tasksPage.map(this::convertToDto);
    }

    @Override
    public TaskResponseDto getTaskByIdForCurrentUser(Long taskId) {
        User currentUser = getCurrentUser();
        log.info("Fetching task with ID: {} for user: {}", taskId, currentUser.getEmail());
        Task task = getTaskForUser(taskId, currentUser.getId());
        return convertToDto(task);
    }

    @Override
    public TaskResponseDto updateTaskForCurrentUser(Long taskId, TaskUpdateRequestDto updateRequest) {
        User currentUser = getCurrentUser();
        log.info("Updating task with ID: {} for user: {}", taskId, currentUser.getEmail());
        Task task = getTaskForUser(taskId, currentUser.getId());

        task.setTitle(updateRequest.title());
        task.setDescription(updateRequest.description());
        task.setDueDate(updateRequest.dueDate());
        task.setPriority(updateRequest.priority());
        task.setStatus(updateRequest.status());

        Task updatedTask = taskRepository.save(task);
        return convertToDto(updatedTask);
    }

    @Override
    public void deleteTaskForCurrentUser(Long taskId) {
        User currentUser = getCurrentUser();
        log.info("Deleting task with ID: {} for user: {}", taskId, currentUser.getEmail());
        Task task = getTaskForUser(taskId, currentUser.getId());
        taskRepository.delete(task);
    }

    // --- YARDIMCI METODLAR ---

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }

    private TaskResponseDto convertToDto(Task task) {
        return new TaskResponseDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                task.getPriority(),
                task.getStatus()
        );
    }

    private Task getTaskForUser(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));

        if (!task.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("User is not authorized to access this task");
        }
        return task;
    }
}