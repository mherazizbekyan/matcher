package com.example.matcher.controller;


import com.example.matcher.controller.model.MatchingResult;
import com.example.matcher.exception.InvalidCsvException;
import com.example.matcher.service.matching.MatchingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class MainController {

    //region Dependencies
    private final MatchingService matchingService;
    //endregion

    //region Constructor
    public MainController(final MatchingService matchingService) {
        this.matchingService = matchingService;
    }
    //endregion

    //region Public methods
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/upload")
    public String uploadAndGetSuggestions(final @RequestParam("file") MultipartFile file, Model model) {

        try {
            final MatchingResult matchingResult = matchingService.calculateBestMatchingCouplesFromCsv(file);
            model.addAttribute("averageMatching", matchingResult.getAveragePercentage());
            model.addAttribute("list", matchingResult.getPairs());
            model.addAttribute("computationTime", matchingResult.getComputationTime());
        } catch (final InvalidCsvException e) {
            model.addAttribute("errorMessage", "Please provide a valid csv file");
            return "index";
        }
        return "suggestion";
    }
    //endregion
}
