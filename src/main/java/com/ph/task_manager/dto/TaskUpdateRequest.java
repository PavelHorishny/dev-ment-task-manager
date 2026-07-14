package com.ph.task_manager.dto;

import jakarta.validation.constraints.NotBlank;

public record TaskUpdateRequest(
    @NotBlank(message = "field cannot be blank") String title, String description) {}
