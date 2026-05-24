package com.umograd.analytic.controller;

import com.umograd.analytic.dto.ParentAgeLimitCustomResponse;
import com.umograd.analytic.dto.ParentAgeLimitResponse;
import com.umograd.analytic.dto.SessionStatusDto;
import com.umograd.analytic.service.LimitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/analytics/limit")
public class LimitController {

    private final LimitService limitService;

    @GetMapping("/parent/check-limit")
    public SessionStatusDto checkLimitBeforeLogin(
            @RequestParam Long childId,
            @RequestParam Long parentId,
            @RequestParam int age) {
        return limitService.checkLimit(childId, parentId, age);
    }

    @GetMapping("/parent/custom-limits")
    public List<ParentAgeLimitCustomResponse> getCustomLimits() {
        return limitService.getCustomLimits();
    }

    @PostMapping("/parent/custom-limits")
    public void saveCustomLimit(@RequestParam Long childId, @RequestParam int minutes) {
        limitService.saveCustomLimit(childId, minutes);
    }

    @GetMapping("/parent/limits")
    public List<ParentAgeLimitResponse> getLimits() {
        return limitService.getLimits();
    }

    @PostMapping("/parent/limits")
    public void saveLimit(@RequestParam int age, @RequestParam int maxMinutes) {
        limitService.saveLimit(age, maxMinutes);
    }
}
