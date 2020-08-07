package com.example.matcher.service.matching;

import com.example.matcher.controller.model.MatchingResult;
import com.example.matcher.exception.InvalidCsvException;
import org.springframework.web.multipart.MultipartFile;

public interface MatchingService {
    MatchingResult calculateBestMatchingCouplesFromCsv(final MultipartFile file) throws InvalidCsvException;
}
