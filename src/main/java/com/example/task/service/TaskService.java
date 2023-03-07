package com.example.task.service;

import com.example.task.model.TaskInfo;
import com.example.task.model.TaskInputData;
import com.example.task.model.TaskResult;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

public interface TaskService {

    @Async
    void createTask(String taskId, String input, String pattern);

    void findBestMatchAsync(TaskInputData taskInputData);

    TaskResult findBestMatch(TaskInputData taskInputData);

    TaskInfo getTaskInfo(String taskId);

    List<TaskInfo> getTasksList();
}
