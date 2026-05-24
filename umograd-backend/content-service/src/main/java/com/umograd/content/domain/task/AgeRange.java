package com.umograd.content.domain.task;

public record AgeRange(int min, int max) {
    public AgeRange {
        if (min < 3 || max > 18) {
            throw new IllegalArgumentException("Возраст должен быть в диапазоне 3–18 лет");
        }
        if (min > max) {
            throw new IllegalArgumentException("Минимальный возраст не может быть больше максимального");
        }
    }

    public boolean includes(int age) {
        return age >= min && age <= max;
    }
}
