package com.example.task.service

import com.example.task.model.TaskInputData
import spock.lang.Specification

class TaskServiceSpec extends Specification {

    TaskService taskService = Mock()

    def "FindBestMatch"() {
        given:
            def taskInputData = createInputData(input, pattern)
        when:
            def taskResult = taskService.findBestMatch(taskInputData)
        then:
            with(taskResult) {
                position == expectedPosition
                typos == expectedTypos
            }
        where:
            input     | pattern | expectedPosition | expectedTypos
            "ABCD"    | "BCD"   | 1                | 0
            "ABCD"    | "BWD"   | 1                | 1
            "ABCDEFG" | "CFG"   | 4                | 1
            "ABCABC"  | "ABC"   | 0                | 0
            "ABCDEFG" | "TDD"   | 1                | 2

    }

    TaskInputData createInputData(String input, String pattern) {
        TaskInputData.builder().input(input).pattern(pattern).build()
    }
}
