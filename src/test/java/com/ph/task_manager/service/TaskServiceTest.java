package com.ph.task_manager.service;

import com.ph.task_manager.dto.TaskCreateRequest;
import com.ph.task_manager.dto.TaskResponse;
import com.ph.task_manager.entity.Task;
import com.ph.task_manager.exception.TaskNotFoundException;
import com.ph.task_manager.repository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    @DisplayName("Create a new task")
    void createTask_ReturnTaskResponse(){
        TaskCreateRequest request = new TaskCreateRequest("Buy milk", "One bottle 3,2%");

        Task savedTask = Task.builder()
                .id(1L)
                .title(request.title())
                .description(request.description())
                .done(false)
                .createdAt(LocalDateTime.now())
                .build();

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        TaskResponse response = taskService.newTask(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Buy milk", response.title());
        assertFalse(response.done());

        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("Get task by ID")
    void getTaskById_ReturnTaskResponse(){
        Long id = 1L;
        Task existingTask =   Task.builder()
                .id(id)
                .title("visit museum")
                .description("visit Szczecin's palace")
                .done(false)
                .createdAt(LocalDateTime.now())
                .build();

        when(taskRepository.findById(id)).thenReturn(Optional.of(existingTask));

        TaskResponse response = taskService.getTaskById(id);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("visit museum", response.title());

        verify(taskRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Exception test")
    void getNonexistentTaskById_ReturnException(){
        Long id = 666L;

        when(taskRepository.findById(id)).thenReturn(Optional.empty());

        TaskNotFoundException exception = assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(id));

        assertEquals("Task ID " + id + " not found", exception.getMessage());
        verify(taskRepository, times(1)).findById(id);
    }
}
