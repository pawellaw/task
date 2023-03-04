package com.example.task.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TaskResult {

    int position;
    int typos;
}
