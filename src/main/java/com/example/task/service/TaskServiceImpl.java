package com.example.task.service;

import com.example.task.model.TaskInputData;
import com.example.task.model.TaskResult;
import com.example.task.model.TaskStatus;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class TaskServiceImpl implements TaskService {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#%");
    private final Map<String, Double> statusMap = new HashMap<>();

    @Async
    @Override
    public void findBestMatchAsync(final TaskInputData taskInputData) {
        final TaskResult bestMatch = findBestMatch(taskInputData);
        System.out.println("bestMatch = " + bestMatch);
    }

    @SneakyThrows
    @Override
    public TaskResult findBestMatch(final TaskInputData taskInputData) {
        System.out.println("Invoking an asynchronous method. "
                + Thread.currentThread().getName());
        final String input = taskInputData.getInput();
        final String pattern = taskInputData.getPattern();
        int inputLength = input.length();
        int patternLength = pattern.length();
        int minTypos = Integer.MAX_VALUE;
        int position = 0;

        //validate Input Data

        final int endOfIteration = inputLength - patternLength;
        for (int i = 0; i <= endOfIteration; i++) {
            final long sleepTime = ThreadLocalRandom.current().nextInt(0, 100);

            final double progress = (double) i / endOfIteration;
            System.out.println("Progress = " + progress + ", SleepTime: " + sleepTime);
            Thread.sleep(sleepTime);
            statusMap.put(taskInputData.getId(), progress);
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

    private TaskResult finishProcessing(final int position, final int typos) {
        System.out.println("Task Finished: " + Thread.currentThread().getName());
        return taskResult(position, typos);
    }

    @Override
    public TaskStatus getTaskStatus(String id) {
        final Double progress = statusMap.get(id);
        return progress != null ?
                TaskStatus.builder()
                        .id(id)
                        .progress(DECIMAL_FORMAT.format(progress))
                        .build()
                : null;
    }

    private TaskResult taskResult(final int position, final int typos) {
        return TaskResult.builder()
                .position(position)
                .typos(typos)
                .build();
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
