package com.ph.task_manager.service;

import com.ph.task_manager.dto.TaskCreateRequest;
import com.ph.task_manager.dto.TaskResponse;
import com.ph.task_manager.dto.TaskUpdateRequest;
import com.ph.task_manager.entity.Task;
import com.ph.task_manager.exception.TaskNotFoundException;
import com.ph.task_manager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {
    private final TaskRepository taskRepository;

    public List<TaskResponse> findAll() {
        return taskRepository.findAll().stream()
                .map(this::convertToResponse)
                .toList();
    }

    public TaskResponse getTaskById(Long id) {
        return taskRepository.findById(id)
                .map(this::convertToResponse)
                .orElseThrow(()->new TaskNotFoundException(id));
    }
    @Transactional
    public TaskResponse newTask(TaskCreateRequest request) {
        Task task = Task.builder().title(request.title())
                .description(request.description())
                .build();
        Task savedTask = taskRepository.save(task);
        return convertToResponse(savedTask);
    }

    public TaskResponse updateTask(Long id, TaskUpdateRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(()->new TaskNotFoundException(id));

        task.setTitle(request.title());
        task.setDescription(request.description());

        return convertToResponse(task);
    }
    @Transactional
    public void deleteTask(Long id) {
        if(!taskRepository.existsById(id)){
            throw new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }

    @Transactional
    public TaskResponse completeTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(()->new TaskNotFoundException(id));
        task.setDone(true);
        return convertToResponse(task);
    }

    private TaskResponse convertToResponse(Task task) {
        return new TaskResponse(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.isDone(),
            task.getCreatedAt()
        );
    }
}
