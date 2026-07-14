package com.ph.task_manager.mapper;

import com.ph.task_manager.dto.TaskResponse;
import com.ph.task_manager.entity.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {
  TaskResponse toResponse(Task task);
}
