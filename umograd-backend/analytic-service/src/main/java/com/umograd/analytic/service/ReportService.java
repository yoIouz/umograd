package com.umograd.analytic.service;

import com.umograd.analytic.dto.ChildProgressPoint;

import java.util.List;

public interface ReportService {

    List<ChildProgressPoint> getChildReport(Long childId, String period);
}
