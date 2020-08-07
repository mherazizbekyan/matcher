package com.example.matcher.controller;


import com.example.matcher.controller.model.Employee;
import com.example.matcher.controller.model.MatchingResult;
import com.example.matcher.service.file.FileService;
import com.example.matcher.service.matching.MatchingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Controller
public class MainController {

    //region Dependencies
    private final FileService fileService;
    private final MatchingService matchingService;
    //endregion

    //region Constructor
    public MainController(final FileService fileService, final MatchingService matchingService) {
        this.fileService = fileService;
        this.matchingService = matchingService;
    }
    //endregion

    //region Public methods
    @PostMapping("/upload")
    public String uploadAndGetSuggestions(final @RequestParam("file") MultipartFile file, Model model) {

        fileService.upload(file);
        List<Employee> employees = null;
        try {
            employees = fileService.extractEmployeesFromCSV();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Objects.nonNull(employees)) {
            final MatchingResult matchingResult = matchingService.calculateBestMatchingCouples(employees);
            model.addAttribute("averageMatching", matchingResult.getAveragePercentage());
            model.addAttribute("list", matchingResult.getPairs());
            model.addAttribute("compytationTime", matchingResult.getComputationTime());
        }
        return "suggestion";
    }
    //endregion
}
