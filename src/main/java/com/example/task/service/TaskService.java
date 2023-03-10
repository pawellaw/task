package com.example.task.service;

import com.example.task.model.TaskInfo;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    /**
     * Create new Task
     *
     * @param input   - input to check
     * @param pattern - pattern to find
     * @return Task identifier
     */
    String createTask(String input, String pattern);

    /**
     * Starts processing of created Task
     *
     * @param taskId - task identifier for task which should be started
     */
    @Async
    void startAsyncProcessing(String taskId);

//    /**
    //     * Task algorithm implementation
    //     *
    //     * @param taskInputData Info about processing Task
    //     * @return TaskResult
    //     */
    //    TaskResult findBestMatch(TaskInputData taskInputData);

    /**
     * Details about Task
     *
     * @param taskId - task identifier
     * @return Task details
     */
    Optional<TaskInfo> getTaskInfo(String taskId);

    /**
     * Return List of all task Details
     *
     * @return Task Details list
     */
    List<TaskInfo> getTasksList();
}
