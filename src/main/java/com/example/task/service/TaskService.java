package com.example.task.service;

import com.example.task.model.TaskInputData;
import com.example.task.model.TaskResult;

public interface TaskService {

    TaskResult findBestMatch(TaskInputData taskInputData);
}
