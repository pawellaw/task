package com.example.task.service

import com.example.task.model.TaskInputData
import spock.lang.Specification

class TaskServiceSpec extends Specification {

    TaskService taskService = new TaskServiceImpl()

    def "Should find best Match"() {
        given:
            def taskId = taskService.createTask(input, pattern)
            def taskInputData = createInputData(taskId, input, pattern)
        when:
            def taskResult = taskService.findBestMatch(taskInputData)
        then:
            with(taskResult) {
                position() == expectedPosition
                typos() == expectedTypos
            }
        where:
            input     | pattern | expectedPosition | expectedTypos
            "ABCD"    | "BCD"   | 1                | 0
            "ABCD"    | "BWD"   | 1                | 1
            "ABCDEFG" | "CFG"   | 4                | 1
            "ABCABC"  | "ABC"   | 0                | 0
            "ABCDEFG" | "TDD"   | 1                | 2

    }

    def "Scenarios for one letter pattern"() {
        given:
            def taskId = taskService.createTask(input, pattern)
            def taskInputData = createInputData(taskId, input, pattern)
        when:
            def taskResult = taskService.findBestMatch(taskInputData)
        then:
            with(taskResult) {
                position() == expectedPosition
                typos() == expectedTypos
            }
        where:
            input                                | pattern | expectedPosition | expectedTypos
            "A"                                  | "A"     | 0                | 0
            "A"                                  | "B"     | 0                | 1
            "AA"                                 | "A"     | 0                | 0
            "AA"                                 | "B"     | 0                | 1
            "AB"                                 | "B"     | 1                | 0
            "BAB"                                | "B"     | 0                | 0
            "AAB"                                | "B"     | 2                | 0
            "AAB"                                | "C"     | 0                | 1
            "A" * 10 + "B" * 10 + "C" * 10 + "D" | "D"     | 30               | 0
    }

    TaskInputData createInputData(String taskId, String input, String pattern) {
        new TaskInputData(taskId, input, pattern)
    }
}
