package com.umograd.analytic.service;

import com.umograd.analytic.dto.AccessResponse;

public interface AccessControlService {

    AccessResponse checkAccess(Long userId);
}
