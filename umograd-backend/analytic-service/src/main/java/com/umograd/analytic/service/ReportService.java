package com.umograd.analytic.service;

import com.umograd.analytic.dto.report.ChildReportDto;

public interface ReportService {

    ChildReportDto getChildReport(Long childId, String period);
}
