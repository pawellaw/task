package com.example.task.api;

import jakarta.validation.constraints.NotEmpty;

public class CreateTaskRequest {

    @NotEmpty(message = "Input should not be empty")
    String input;
    @NotEmpty(message = "Pattern should not be empty")
    String pattern;

    public CreateTaskRequest(final String input, final String pattern) {
        this.input = input;
        this.pattern = pattern;
    }

    public String input() {
        return input;
    }

    public String pattern() {
        return pattern;
    }

}

