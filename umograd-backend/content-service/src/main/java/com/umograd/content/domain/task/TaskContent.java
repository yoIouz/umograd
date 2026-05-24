package com.umograd.content.domain.task;

import java.util.List;

public class TaskContent {
    private final List<Question> questions;

    public TaskContent(List<Question> questions) {
//        if (questions == null || questions.isEmpty()) {
//            throw new IllegalArgumentException("Task must have at least one question");
//        }
        this.questions = List.copyOf(questions);
    }

    public List<Question> questions() {
        return questions;
    }
}
