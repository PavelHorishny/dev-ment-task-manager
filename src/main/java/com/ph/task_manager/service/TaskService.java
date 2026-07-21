package com.ph.task_manager.service;

import com.ph.task_manager.dto.TaskCreateRequest;
import com.ph.task_manager.dto.TaskResponse;
import com.ph.task_manager.dto.TaskUpdateRequest;
import com.ph.task_manager.entity.Task;
import com.ph.task_manager.exception.TaskNotFoundException;
import com.ph.task_manager.mapper.TaskMapper;
import com.ph.task_manager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskService {

  private static final int MAX_PAGE_SIZE = 50;
  private final TaskRepository taskRepository;
  private final TaskMapper taskMapper;

  @Transactional(readOnly = true)
  public Page<TaskResponse> findAll(Pageable pageable) {
    Pageable limitedPageable = pageable.getPageSize() > MAX_PAGE_SIZE
            ? PageRequest.of(pageable.getPageNumber(), MAX_PAGE_SIZE, pageable.getSort())
            : pageable;
    return taskRepository.findAll(limitedPageable).map(taskMapper::toResponse);
  }

  @Transactional(readOnly = true)
  public TaskResponse getTaskById(Long id) {
    return taskRepository
        .findById(id)
        .map(taskMapper::toResponse)
        .orElseThrow(() -> new TaskNotFoundException(id));
  }

  @Transactional
  public TaskResponse newTask(TaskCreateRequest request) {
    Task task = Task.builder().title(request.title()).description(request.description()).build();
    Task savedTask = taskRepository.save(task);
    return taskMapper.toResponse(savedTask);
  }

  @Transactional
  public TaskResponse updateTask(Long id, TaskUpdateRequest request) {

    Task task = ifTaskExists(id);

    task.setTitle(request.title());
    task.setDescription(request.description());

    return taskMapper.toResponse(task);
  }

  public TaskResponse updateTask(Long id, boolean done){
    Task task = ifTaskExists(id);

    task.setDone(done);
    return taskMapper.toResponse(task);
  }

  @Transactional
  public void deleteTask(Long id) {
    if (!taskRepository.existsById(id)) {
      throw new TaskNotFoundException(id);
    }
    taskRepository.deleteById(id);
  }


  private Task ifTaskExists(Long id){
    return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
  }
}
