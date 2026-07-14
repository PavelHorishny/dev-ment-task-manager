package com.ph.task_manager.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.ph.task_manager.dto.TaskCreateRequest;
import com.ph.task_manager.dto.TaskResponse;
import com.ph.task_manager.entity.Task;
import com.ph.task_manager.exception.TaskNotFoundException;
import com.ph.task_manager.fixtures.TaskFixtures;
import com.ph.task_manager.mapper.TaskMapper;
import com.ph.task_manager.repository.TaskRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
  @Mock private TaskRepository taskRepository;
  @Mock private TaskMapper mapper;
  @InjectMocks private TaskService taskService;

  @Test
  @DisplayName("Create a new task")
  void createTask_ReturnTaskResponse() {
    TaskCreateRequest request = new TaskCreateRequest("Buy milk", "one bottle 3,2%");

    Task savedTask = TaskFixtures.defaultTask();

    when(taskRepository.save(any(Task.class))).thenReturn(savedTask);
    when(mapper.toResponse(savedTask))
        .thenReturn(
            new TaskResponse(
                savedTask.getId(),
                savedTask.getTitle(),
                savedTask.getDescription(),
                savedTask.isDone(),
                savedTask.getCreatedAt()));
    TaskResponse response = taskService.newTask(request);

    assertNotNull(response);
    assertEquals(savedTask.getId(), response.id());
    assertEquals(savedTask.getTitle(), response.title());
    assertEquals(savedTask.getDescription(), response.description());
    assertFalse(response.done());

    verify(taskRepository, times(1)).save(any(Task.class));
  }

  @Test
  @DisplayName("Get task by ID")
  void getTaskById_ReturnTaskResponse() {
    Long id = 1L;
    Task existingTask = TaskFixtures.defaultTask();

    when(taskRepository.findById(id)).thenReturn(Optional.of(existingTask));
    when(mapper.toResponse(existingTask))
        .thenReturn(
            new TaskResponse(
                existingTask.getId(),
                existingTask.getTitle(),
                existingTask.getDescription(),
                existingTask.isDone(),
                existingTask.getCreatedAt()));

    when(taskRepository.findById(id)).thenReturn(Optional.of(existingTask));
    when(mapper.toResponse(existingTask))
        .thenReturn(
            new TaskResponse(
                existingTask.getId(),
                existingTask.getTitle(),
                existingTask.getDescription(),
                existingTask.isDone(),
                existingTask.getCreatedAt()));

    TaskResponse response = taskService.getTaskById(id);

    assertNotNull(response);
    assertEquals(1L, response.id());
    assertEquals(existingTask.getTitle(), response.title());

    verify(taskRepository, times(1)).findById(id);
  }

  @Test
  @DisplayName("Exception test")
  void getNonexistentTaskById_ReturnException() {

    Task task = TaskFixtures.defaultTask().withId(666L);

    when(taskRepository.findById(task.getId())).thenReturn(Optional.empty());

    TaskNotFoundException exception =
        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(task.getId()));

    assertEquals("Task ID " + task.getId() + " not found", exception.getMessage());
    verify(taskRepository, times(1)).findById(task.getId());
  }
}
