package com.umograd.analytic.controller;

import com.umograd.analytic.dto.achievement.AchievementGrantResponse;
import com.umograd.analytic.dto.achievement.AchievementResponse;
import com.umograd.analytic.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/analytics/achievements")
@RequiredArgsConstructor
public class AchievementController {

    private final AchievementService achievementService;

    @PostMapping("/process/{childId}")
    public List<AchievementGrantResponse> process(@PathVariable Long childId) {
        return achievementService.checkAndGrant(childId);
    }

    @GetMapping("/child/{childId}")
    public List<Long> getEarnedAchievementIds(@PathVariable Long childId) {
        return achievementService.getEarnedAchievementIds(childId);
    }

    @GetMapping
    public List<AchievementResponse> getAchievements() {
        return achievementService.getAchievements();
    }
}
