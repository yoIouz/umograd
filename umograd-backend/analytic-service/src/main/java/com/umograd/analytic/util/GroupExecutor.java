package com.umograd.analytic.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GroupExecutor {

    public int getTargetAgeGroup(int age) {
        int targetAgeGroup = 18;
        if (age <= 5) targetAgeGroup = 5;
        else if (age <= 8) targetAgeGroup = 8;
        else if (age <= 10) targetAgeGroup = 10;
        else if (age <= 12) targetAgeGroup = 12;
        else if (age <= 14) targetAgeGroup = 14;
        else if (age <= 16) targetAgeGroup = 16;

        return targetAgeGroup;
    }
}
