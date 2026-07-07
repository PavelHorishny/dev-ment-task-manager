package com.ph.task_manager.controller;

import com.ph.task_manager.dto.TaskCreateRequest;
import com.ph.task_manager.dto.TaskResponse;
import com.ph.task_manager.dto.TaskUpdateRequest;
import com.ph.task_manager.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public List<TaskResponse> findAll() {
        return taskService.findAll();
    }

    @GetMapping("/{id}")
    public TaskResponse getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @PostMapping
    @ResponseStatus (HttpStatus.CREATED)
    public TaskResponse createNewTask(@Valid @RequestBody TaskCreateRequest request){
        return taskService.newTask(request);
    }

    @PutMapping("/{id}")
    public TaskResponse updateTask(@PathVariable Long id,
                                   @Valid @RequestBody TaskUpdateRequest request){
        return taskService.updateTask(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus (HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long id){
        taskService.deleteTask(id);
    }

    @PatchMapping("/{id}/complete")
    public TaskResponse completeTask(@PathVariable Long id){
        return taskService.completeTask(id);
    }
}
