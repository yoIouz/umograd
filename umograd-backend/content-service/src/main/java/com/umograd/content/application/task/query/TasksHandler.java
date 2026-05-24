package com.umograd.content.application.task.query;

public interface TasksHandler <T, R> {

    T handle(R query);
}
