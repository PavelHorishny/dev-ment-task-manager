package com.ph.task_manager.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(Long id) {
    super("Task ID " + id + " not found");
    }
}
