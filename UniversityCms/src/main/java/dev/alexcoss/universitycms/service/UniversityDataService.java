package dev.alexcoss.universitycms.service;

import dev.alexcoss.universitycms.dto.data.GenerateDataRequest;

public interface UniversityDataService {

    void saveUniversityData();

    void saveUniversityData(GenerateDataRequest dataRequest);
}
