package com.umograd.analytic.controller;

import com.umograd.analytic.dto.AccessResponse;
import com.umograd.analytic.service.AccessControlService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/analytics/access")
@RequiredArgsConstructor
public class AccessController {

    private final AccessControlService accessControlService;

    @GetMapping("/check/{userId}")
    public AccessResponse checkAccess(@PathVariable Long userId) {
        return accessControlService.checkAccess(userId);
    }
}
