package com.example.matcher.service.matching.criteria;

import com.example.matcher.controller.model.Employee;

public interface MatchingCriteriaProvider {
    double calculateMatchingWeight(Employee leftNode, Employee rightNode);
}
