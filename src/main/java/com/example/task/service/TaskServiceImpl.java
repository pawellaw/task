package com.example.task.service;

import com.example.task.model.TaskInfo;
import com.example.task.model.TaskInputData;
import com.example.task.model.TaskResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class TaskServiceImpl implements TaskService {

    private final Map<String, TaskInfo> tasks = new ConcurrentHashMap<>();

    @Async
    @Override
    public void createTask(final String taskId, final String input, final String pattern) {
        TaskInfo taskInfo = new TaskInfo(taskId, input, pattern);
        tasks.put(taskId, taskInfo);
        TaskInputData taskInputData = new TaskInputData(taskId, input, pattern);
        findBestMatchAsync(taskInputData);
    }

    @Override
    public void findBestMatchAsync(final TaskInputData taskInputData) {
        startProgressOnTask(taskInputData.id());
        final TaskResult taskResult = findBestMatch(taskInputData);
        System.out.println("taskResult = " + taskResult);
        finishTask(taskInputData.id(), taskResult);
    }

    private void startProgressOnTask(final String taskId) {
        tasks.get(taskId).startProgress();
    }


    private void finishTask(final String taskId, final TaskResult taskResult) {
        tasks.get(taskId).finishProgress(taskResult.position(), taskResult.typos());
    }

    @Override
    public TaskResult findBestMatch(final TaskInputData taskInputData) {
        System.out.println("Invoking an asynchronous method. "
                + Thread.currentThread().getName());
        final String input = taskInputData.input();
        final String pattern = taskInputData.pattern();
        final String taskId = taskInputData.id();

        int inputLength = input.length();
        int patternLength = pattern.length();
        int minTypos = Integer.MAX_VALUE;
        int position = 0;

        //validate Input Data

        final int endOfIteration = inputLength - patternLength;
        for (int i = 0; i <= endOfIteration; i++) {
            final long sleepTime = ThreadLocalRandom.current().nextInt(0, 1000);

            final double progress = (double) i / endOfIteration;
            System.out.println("Progress = " + progress + ", SleepTime: " + sleepTime);
            sleep(sleepTime);
            tasks.get(taskId).updateProgress(progress);

            int typos = checkTypos(input.substring(i, patternLength + i), pattern);
            if (typos == 0) {
                return finishProcessing(i, typos);
            }
            if (typos < minTypos) {
                minTypos = typos;
                position = i;
            }
        }
        return finishProcessing(position, minTypos);
    }

    private static void sleep(final long sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private TaskResult finishProcessing(final int position, final int typos) {
        System.out.println("Task Finished: " + Thread.currentThread().getName());
        return taskResult(position, typos);
    }

    @Override
    public TaskInfo getTaskInfo(String taskId) {
        return tasks.get(taskId);
    }

    @Override
    public List<TaskInfo> getTasksList() {
        return tasks.values().stream().toList();
    }

    private TaskResult taskResult(final int position, final int typos) {
        return new TaskResult(position, typos);
    }

    private int checkTypos(final String substring, final String pattern) {
        int typos = 0;
        for (int i = 0; i < pattern.length(); i++) {
            if (substring.charAt(i) != pattern.charAt(i)) {
                typos++;
            }
        }
        return typos;
    }
}
