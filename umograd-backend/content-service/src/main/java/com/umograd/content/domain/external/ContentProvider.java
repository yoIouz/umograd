package com.umograd.content.domain.external;

import java.util.List;

public interface ContentProvider {
    /**
     * Получить список заданий по теме из внешнего источника.
     *
     * @param topic тема (например, "math", "reading")
     * @param limit ограничение по количеству
     * @return список DTO с полным содержимым задания
     */
    List<ExternalTaskDto> fetchTasks(String topic, int limit);
}
