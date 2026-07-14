package com.ph.task_manager.fixtures;

import com.ph.task_manager.entity.Task;

import java.time.LocalDateTime;

public class TaskFixtures {
  public static Task defaultTask() {
    return Task.builder()
        .id(1L)
        .title("Buy milk")
        .description("One bottle 3,2%")
        .done(false)
        .createdAt(LocalDateTime.now())
        .build();
  }
}
