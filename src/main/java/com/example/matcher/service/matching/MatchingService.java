package com.example.matcher.service.matching;

import com.example.matcher.controller.model.Employee;
import com.example.matcher.controller.model.MatchingResult;
import com.example.matcher.exception.UnprocessableCsvFileException;
import com.example.matcher.exception.WrongFileExtensionException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MatchingService {
    MatchingResult calculateBestMatchingCouplesFromCsv(final MultipartFile file)
            throws UnprocessableCsvFileException, WrongFileExtensionException;
}
