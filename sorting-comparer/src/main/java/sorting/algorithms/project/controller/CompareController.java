package sorting.algorithms.project.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import sorting.algorithms.project.dto.AlgorithmInfo;
import sorting.algorithms.project.dto.CompareRequest;
import sorting.algorithms.project.dto.SortResult;
import sorting.algorithms.project.service.SortingService;
import sorting.algorithms.project.SortingAlgorithms.SortingAlgorithm;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * REST controller for API endpoints related to comparing and visualizing sorting algorithms.
 */
@RestController
@RequestMapping("/api/compare") // Base path for all endpoints in this controller
@CrossOrigin // Allows requests from different origins (e.g., frontend running on a different port)
public class CompareController {

    private final SortingService sortingService;

    /**
     * Constructor for CompareController, injecting the SortingService dependency.
     * @param sortingService The service responsible for managing and executing sorting algorithms.
     */
    @Autowired
    public CompareController(SortingService sortingService) {
        this.sortingService = sortingService;
    }

    /**
     * POST endpoint to compare multiple sorting algorithms based on a request.
     * Accepts a JSON request body containing algorithm names and input data.
     * @param request The CompareRequest object containing the list of algorithms and the input list.
     * @return A list of SortResult objects, one for each requested algorithm, containing performance metrics and results.
     */
    @PostMapping
    public List<SortResult> compareAlgorithms(@RequestBody CompareRequest request) {
        return sortingService.compare(request);
    }

    /**
     * GET endpoint to retrieve information about all available sorting algorithms.
     * @return A list of AlgorithmInfo objects, each describing an available algorithm (name, complexity).
     */
    @GetMapping("/algorithms")
    public List<AlgorithmInfo> availableAlgorithms() {
        return sortingService.getAvailableAlgorithms();
    }


    /**
     * GET endpoint to generate standard datasets (unsorted, sorted, reverse, etc.).
     * @param count The desired number of elements in the generated datasets. Defaults to 5, clamped between 5 and 1,000,000.
     * @return A map where keys are dataset names (e.g., "unsorted") and values are lists of integers.
     */
    @GetMapping("/datasets")
    public Map<String, List<Integer>> getDatasets(
            @RequestParam(required = false, defaultValue = "5") int count) {

        Map<String, List<Integer>> sets = new LinkedHashMap<>(); // Use LinkedHashMap to preserve insertion order

        // Ensure element count is within reasonable bounds
        final int ELEMENT_COUNT = Math.max(5, Math.min(count, 1000000));
        // Use element count - 1 as max value for simple range generation
        final int MAX_VALUE = ELEMENT_COUNT > 0 ? ELEMENT_COUNT - 1 : 0;

        // Generate ascending list (0 to ELEMENT_COUNT-1)
        List<Integer> ascending = new ArrayList<>();
        for (int i = 0; i < ELEMENT_COUNT; i++) {
            ascending.add(i);
        }

        // Generate descending list (MAX_VALUE down to 0)
        List<Integer> descending = new ArrayList<>();
        for (int i = MAX_VALUE; i >= 0; i--) {
            descending.add(i);
        }

        // Generate half-sorted list (first half sorted, second half shuffled)
        List<Integer> halfSorted = new ArrayList<>(ascending);
        if (ELEMENT_COUNT >= 2) {
            int midPoint = ELEMENT_COUNT / 2;
            // Get a view of the second half, copy it, shuffle the copy, then update the original list
            List<Integer> secondHalf = new ArrayList<>(halfSorted.subList(midPoint, ELEMENT_COUNT));
            Collections.shuffle(secondHalf);
            for (int i = 0; i < secondHalf.size(); i++) {
                halfSorted.set(midPoint + i, secondHalf.get(i));
            }
        }

        // Generate unsorted list (shuffle the ascending list)
        List<Integer> unsorted = new ArrayList<>(ascending);
        Collections.shuffle(unsorted);

        // Add generated datasets to the map
        sets.put("unsorted", unsorted);
        sets.put("halfSorted", halfSorted);
        sets.put("sorted", ascending);
        sets.put("reverse", descending);
        return sets;
    }

    /**
     * GET endpoint to initiate a Server-Sent Events (SSE) stream for live visualization
     * of a specific sorting algorithm execution.
     * @param algorithm The name of the algorithm to visualize.
     * @param dataset The name of a standard dataset to use (optional, e.g., "unsorted", "sorted").
     * @param input A URL-encoded JSON array string representing custom input data (optional, overrides 'dataset').
     * @param speed The delay in milliseconds between sending sort steps (controls visualization speed). Defaults to 1.
     * @param count The element count to use if generating a standard 'dataset' or the default unsorted list. Defaults to 5.
     * @return An SseEmitter instance that streams SortStep objects to the client.
     */
    @GetMapping("/visualizer/{algorithm}")
    public SseEmitter visualize(@PathVariable String algorithm,
                                @RequestParam(required = false) String dataset,
                                @RequestParam(required = false) String input,
                                @RequestParam(required = false, defaultValue = "1") int speed,
                                @RequestParam(required = false, defaultValue = "5") int count
    ) {

        // Create an SseEmitter with no timeout (0L)
        SseEmitter emitter = new SseEmitter(0L);
        // Find the requested sorting algorithm implementation
        SortingAlgorithm algo = sortingService.getAlgorithmByName(algorithm);

        // Determine the input data list
        List<Integer> inputData = new ArrayList<>(); // Default to empty list

        // Priority: custom input > dataset name > algorithm default > default unsorted
        if (input != null && !input.isEmpty()) {
            // Try parsing custom input JSON string
            try {
                // Decode the URL-encoded string
                String decoded = URLDecoder.decode(input, StandardCharsets.UTF_8.name());
                ObjectMapper mapper = new ObjectMapper();
                // Parse the JSON array into a List<Integer>
                List<Integer> parsed = mapper.readValue(decoded, new TypeReference<List<Integer>>() {});
                inputData = new ArrayList<>(parsed);
            } catch (Exception e) {
                // Log error if parsing fails, will fall back to other options
                e.printStackTrace();
            }
        }

        // If no custom input was successfully parsed, check for dataset name
        if (inputData.isEmpty() && dataset != null && !dataset.isEmpty()) {
            // Generate standard datasets based on 'count'
            Map<String, List<Integer>> standardSets = this.getDatasets(count);
            // Get the requested dataset
            List<Integer> ds = standardSets.get(dataset);
            if (ds != null) {
                inputData = new ArrayList<>(ds);
            }
        }

        // If still no data, try the algorithm's default dataset
        if (inputData.isEmpty()) {
            List<Integer> algoDefaultData = sortingService.getDatasetByName(algorithm);
            if (algoDefaultData != null && !algoDefaultData.isEmpty()) {
                inputData = new ArrayList<>(algoDefaultData);
            }
        }

        // Final fallback: generate a default unsorted list if nothing else worked
        if (inputData.isEmpty()) {
            inputData = this.getDatasets(count).getOrDefault("unsorted", new ArrayList<>());
        }


        // Flag to control the sending loop in the separate thread
        AtomicBoolean active = new AtomicBoolean(true);

        // Define emitter callbacks for completion, timeout, and error
        emitter.onCompletion(() -> {
            active.set(false); // Stop sending if connection completes
            System.out.println("SSE completed (onCompletion).");
        });
        emitter.onTimeout(() -> {
            active.set(false); // Stop sending on timeout
            try { emitter.complete(); } catch (Exception ignore) {} // Attempt to complete emitter
            System.out.println("SSE timed out (onTimeout).");
        });
        emitter.onError(err -> {
            active.set(false); // Stop sending on error
            System.err.println("SSE error: " + err);
        });

        // If the algorithm was found, start the sorting in a new thread
        if (algo != null) {
            // Create a mutable copy for the sorting algorithm to modify
            List<Integer> mutableInput = new ArrayList<>(inputData);
            new Thread(() -> {
                try {
                    // Execute the sort with the callback mechanism
                    algo.sortWithCallback(mutableInput, step -> {
                        // Check if the emitter is still active before sending
                        if (!active.get()) return;

                        try {
                            // Send the current sort step to the client
                            emitter.send(step);

                            // Pause execution to control visualization speed
                            if (speed > 0) {
                                try {
                                    Thread.sleep(speed);
                                } catch (InterruptedException ie) {
                                    // Restore interrupted status if thread is interrupted
                                    Thread.currentThread().interrupt();
                                }
                            }

                        } catch (IllegalStateException ise) {
                            // Emitter might be completed concurrently, stop sending
                            active.set(false);
                            System.out.println("Emitter already completed, stopping sends.");
                        } catch (Exception e) {
                            // Handle other potential errors during send
                            active.set(false);
                            try { emitter.completeWithError(e); } catch (Exception ignore) {}
                            System.err.println("Error while sending SSE event: " + e);
                        }
                    });

                    // After sorting completes, if still active, complete the emitter
                    if (active.get()) {
                        try { emitter.complete(); } catch (Exception ignore) {}
                    }
                } catch (Exception e) {
                    // Handle errors during the sortWithCallback execution itself
                    active.set(false);
                    try { emitter.completeWithError(e); } catch (Exception ignore) {}
                }
            }).start(); // Start the sorting thread
        } else {
            // If algorithm not found, complete the emitter immediately
            try { emitter.complete(); } catch (Exception ignore) {}
        }

        // Return the emitter to the client
        return emitter;
    }
}