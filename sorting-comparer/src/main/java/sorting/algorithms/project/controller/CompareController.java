package sorting.algorithms.project.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import sorting.algorithms.project.dto.CompareRequest;
import sorting.algorithms.project.dto.SortResult;
import sorting.algorithms.project.service.SortingService;

import java.util.List;

@RestController
@RequestMapping("/api/compare")
@CrossOrigin
public class CompareController {

    private final SortingService sortingService;

    @Autowired
    public CompareController(SortingService sortingService) {
        this.sortingService = sortingService;
    }

    @PostMapping
    public List<SortResult> compareAlgorithms(@RequestBody CompareRequest request) {
        return sortingService.compare(request);
    }

    @GetMapping("/algorithms")
    public List<String> availableAlgorithms() {
        return sortingService.getAvailableAlgorithms();
    }
}