package com.umograd.analytic.service;

import com.umograd.analytic.dto.ParentAgeLimitCustomResponse;
import com.umograd.analytic.dto.ParentAgeLimitResponse;
import com.umograd.analytic.dto.SessionStatusDto;

import java.util.List;

public interface LimitService {

    SessionStatusDto checkLimit(Long childId, Long parentId, int age);

    List<ParentAgeLimitResponse> getLimits();

    void saveLimit(int age, int maxMinutes);

    void saveCustomLimit(Long childId, int minutes);

    List<ParentAgeLimitCustomResponse> getCustomLimits();
}
