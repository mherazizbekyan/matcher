package com.example.matcher.service.matching;

import com.example.matcher.controller.model.Employee;
import com.example.matcher.controller.model.MatchingResult;

import java.util.List;

public interface MatchingService {
    MatchingResult calculateBestMatchingCouples(List<Employee> employees);
}
