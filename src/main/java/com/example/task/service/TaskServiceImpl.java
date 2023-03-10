package com.example.task.service;

import com.example.task.api.TaskControler;
import com.example.task.model.TaskInfo;
import com.example.task.model.TaskInputData;
import com.example.task.model.TaskResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class TaskServiceImpl implements TaskService {

    private final Logger logger = LoggerFactory.getLogger(TaskControler.class);

    @Autowired
    public TaskServiceImpl(final TaskStorage taskStorage, final AlgorithmProcessor algorithmProcessor) {
        this.taskStorage = taskStorage;
        this.algorithmProcessor = algorithmProcessor;
    }

    private final TaskStorage taskStorage;

    private final AlgorithmProcessor algorithmProcessor;

    @Override
    public String createTask(final String input, final String pattern) {
        final String taskId = UUID.randomUUID().toString();
        TaskInfo taskInfo = new TaskInfo(taskId, input, pattern);
        taskStorage.put(taskId, taskInfo);
        return taskId;
    }

    @Async
    @Override
    public void startAsyncProcessing(final String taskId) {
        TaskInputData taskInputData = createTaskInputData(taskId);
        startProgressOnTask(taskInputData.id());
        final TaskResult taskResult = algorithmProcessor.findBestMatch(taskInputData);
        logger.info("TaskResult = " + taskResult + " for taskId " + taskId);
        finishTask(taskInputData.id(), taskResult);
    }

    private TaskInputData createTaskInputData(final String taskId) {
        final TaskInfo taskInfo = taskStorage.get(taskId);
        return new TaskInputData(taskId, taskInfo.getInput(), taskInfo.getPattern());
    }

    private void startProgressOnTask(final String taskId) {
        taskStorage.get(taskId).startProgress();
    }

    private void finishTask(final String taskId, final TaskResult taskResult) {
        taskStorage.get(taskId).finishProgress(taskResult.position(), taskResult.typos());
    }

    @Override
    public Optional<TaskInfo> getTaskInfo(String taskId) {
        return Optional.ofNullable(taskStorage.get(taskId));
    }

    @Override
    public List<TaskInfo> getTasksList() {
        return taskStorage.getList();
    }
}
