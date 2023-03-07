package com.example.task.model;

public class TaskInfo {
    String id;
    String input;
    String pattern;
    long creationTime;
    long startProgressTime;
    double progress;
    long finishTime;
    TaskStatus status = TaskStatus.CREATED;
    int position;
    int typos;

    public TaskInfo(final String taskId, final String input, final String pattern) {
        this.id = taskId;
        this.input = input;
        this.pattern = pattern;
        this.creationTime = System.currentTimeMillis();
    }

    public void startProgress() {
        this.status = TaskStatus.INPROGRESS;
        this.startProgressTime = System.currentTimeMillis();
    }

    public void updateProgress(final double progress) {
        this.progress = progress;
    }

    public void finishProgress(final int position, final int typos) {
        this.position = position;
        this.typos = typos;
        this.finishTime = System.currentTimeMillis();
        this.status = TaskStatus.FINISHED;
    }

    public String getId() {
        return id;
    }

    public String getInput() {
        return input;
    }

    public String getPattern() {
        return pattern;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public long getStartProgressTime() {
        return startProgressTime;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public int getPosition() {
        return position;
    }

    public int getTypos() {
        return typos;
    }

    public double getProgress() {
        return progress;
    }
}
