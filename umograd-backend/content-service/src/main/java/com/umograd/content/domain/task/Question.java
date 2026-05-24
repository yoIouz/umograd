package com.umograd.content.domain.task;

import java.util.List;

public record Question  (
        String contentType,
        String question,
        List<String> options,
        String answer,
        String hint
){
}
