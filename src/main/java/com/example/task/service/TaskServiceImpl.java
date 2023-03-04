package com.example.task.service;

import com.example.task.model.TaskInputData;
import com.example.task.model.TaskResult;

public class TaskServiceImpl implements TaskService {

    @Override
    public TaskResult findBestMatch(final TaskInputData taskInputData) {
        final String input = taskInputData.getInput();
        final String pattern = taskInputData.getPattern();
        int inputLength = input.length();
        int patternLength = pattern.length();
        int minTypos = Integer.MAX_VALUE;
        int position = 0;

        //validate Input Data

        for (int i = 0; i <= inputLength - patternLength; i++) {
            int typos = checkTypos(input.substring(i, patternLength + i), pattern);
            if (typos == 0) {
                return taskResult(i, typos);
            }
            if (typos < minTypos) {
                minTypos = typos;
                position = i;
            }
        }
        return taskResult(position, minTypos);
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
