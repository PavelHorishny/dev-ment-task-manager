package com.ph.task_manager.controller;

import com.ph.task_manager.dto.TaskCreateRequest;
import com.ph.task_manager.dto.TaskResponse;
import com.ph.task_manager.dto.TaskUpdateRequest;
import com.ph.task_manager.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<Page<TaskResponse>> findAll(
            @PageableDefault(size = 2, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(taskService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public TaskResponse getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse createNewTask(@Valid @RequestBody TaskCreateRequest request) {
        return taskService.newTask(request);
    }

    @PutMapping("/{id}")
    public TaskResponse updateTask(@PathVariable Long id,
                                   @Valid @RequestBody TaskUpdateRequest request) {
        return taskService.updateTask(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    @PatchMapping("/{id}/complete")
    public TaskResponse completeTask(@PathVariable Long id) {
        return taskService.completeTask(id);
    }
}
