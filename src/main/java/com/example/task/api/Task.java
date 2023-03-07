package com.example.task.api;

public record Task(String id,
                   String input,
                   String pattern,
                   long creationTime,
                   long startProgressTime,
                   long finishTime,
                   String status,
                   String progress,
                   String position,
                   String typos) {
}
