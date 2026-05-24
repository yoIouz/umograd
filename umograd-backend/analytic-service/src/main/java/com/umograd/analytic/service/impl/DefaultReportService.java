package com.umograd.analytic.service.impl;

import com.umograd.analytic.dto.ChildProgressPoint;
import com.umograd.analytic.repository.task.TaskResultRepository;
import com.umograd.analytic.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultReportService implements ReportService {

    private final TaskResultRepository taskResultRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ChildProgressPoint> getChildReport(Long childId, String period) {
        return taskResultRepository.getAdvancedChildProgressHistory(childId, period.toLowerCase()).stream()
                .map(row -> new ChildProgressPoint(
                        (String) row[0],
                        ((Number) row[1]).doubleValue(),
                        ((Number) row[2]).doubleValue(),
                        (String) row[3]
                ))
                .collect(Collectors.toList());
    }
}
