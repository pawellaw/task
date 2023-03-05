package com.example.task.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class TaskResult {
    int position;
    int typos;
}
