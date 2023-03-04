package com.example.task.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TaskInputData {
    String input;
    String pattern;
}