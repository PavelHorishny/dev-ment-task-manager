package com.ph.task_manager.service;

import com.ph.task_manager.dto.TaskCreateRequest;
import com.ph.task_manager.dto.TaskResponse;
import com.ph.task_manager.dto.TaskUpdateRequest;
import com.ph.task_manager.entity.Task;
import com.ph.task_manager.exception.TaskNotFoundException;
import com.ph.task_manager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    @Transactional(readOnly = true)
    public Page<TaskResponse> findAll(Pageable pageable) {
        return taskRepository.findAll(pageable)
                .map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
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

    @Transactional
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
