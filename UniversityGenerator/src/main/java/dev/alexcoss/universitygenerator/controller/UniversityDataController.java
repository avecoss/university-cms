package dev.alexcoss.universitygenerator.controller;

import dev.alexcoss.universitygenerator.dto.GenerateDataRequest;
import dev.alexcoss.universitygenerator.dto.UniversityData;
import dev.alexcoss.universitygenerator.service.manager.StartingDataManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/university-data")
public class UniversityDataController {

    private final StartingDataManager startingDataManager;

    @PostMapping("/generate")
    public ResponseEntity<UniversityData> generateData(@RequestBody @Valid GenerateDataRequest request,
                                                       BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors())
            throw new BindException(bindingResult);

        return ResponseEntity.status(HttpStatus.OK)
            .body(startingDataManager.generateUniversityData(request));
    }

    @PostMapping("/static")
    public ResponseEntity<UniversityData> generateData() {

        return ResponseEntity.status(HttpStatus.OK)
            .body(startingDataManager.getUniversityData());
    }
}
