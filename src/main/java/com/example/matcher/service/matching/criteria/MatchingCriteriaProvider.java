package com.example.matcher.service.matching.criteria;

import com.example.matcher.controller.model.Employee;

public interface MatchingCriteriaProvider {
    double calculateMatchingWeight(final Employee leftNode, final Employee rightNode);

    boolean areLocationPreferencesCompatible(final Employee leftNode, final Employee rightNode);
}
