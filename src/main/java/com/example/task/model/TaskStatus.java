package com.example.task.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
@Getter
public class TaskStatus {
    String id;
    String progress;
}
