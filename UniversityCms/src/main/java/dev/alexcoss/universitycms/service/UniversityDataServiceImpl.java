package dev.alexcoss.universitycms.service;

import dev.alexcoss.universitycms.dto.data.GenerateDataRequest;
import dev.alexcoss.universitycms.dto.data.UniversityDataResponse;
import dev.alexcoss.universitycms.service.client.DataRestClient;
import dev.alexcoss.universitycms.service.manager.UniversityDataManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UniversityDataServiceImpl implements UniversityDataService {

    private final DataRestClient dataRestClient;
    private final UniversityDataManager universityDataManager;

    @Override
    public void saveUniversityData() {
        UniversityDataResponse universityData = dataRestClient.getStaticData();
        universityDataManager.saveData(universityData);
    }

    @Override
    public void saveUniversityData(GenerateDataRequest dataRequest) {
        UniversityDataResponse universityData = dataRestClient.getDynamicData(dataRequest);
        universityDataManager.saveData(universityData);
    }
}
