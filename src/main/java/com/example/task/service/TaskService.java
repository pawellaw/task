package com.example.task.service;

import com.example.task.model.TaskInputData;
import com.example.task.model.TaskResult;
import com.example.task.model.TaskStatus;
import org.springframework.scheduling.annotation.Async;

public interface TaskService {

    @Async
    void findBestMatchAsync(TaskInputData taskInputData);

    TaskResult findBestMatch(TaskInputData taskInputData);

    TaskStatus getTaskStatus(String id);
}
