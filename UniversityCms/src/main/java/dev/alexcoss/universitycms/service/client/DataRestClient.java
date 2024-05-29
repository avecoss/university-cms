package dev.alexcoss.universitycms.service.client;

import dev.alexcoss.universitycms.dto.data.GenerateDataRequest;
import dev.alexcoss.universitycms.dto.data.UniversityDataResponse;

public interface DataRestClient {

    UniversityDataResponse getStaticData();

    UniversityDataResponse getDynamicData(GenerateDataRequest dataRequest);
}
