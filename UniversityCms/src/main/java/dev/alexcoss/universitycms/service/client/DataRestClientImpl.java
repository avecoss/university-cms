package dev.alexcoss.universitycms.service.client;

import dev.alexcoss.universitycms.dto.data.GenerateDataRequest;
import dev.alexcoss.universitycms.dto.data.UniversityDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
public class DataRestClientImpl implements DataRestClient {

    private final RestClient restClient;

    @Override
    public UniversityDataResponse getStaticData() {
        return restClient.get()
            .uri("/api/university-data/static")
            .retrieve()
            .body(UniversityDataResponse.class);
    }

    @Override
    public UniversityDataResponse getDynamicData(GenerateDataRequest dataRequest) {
        return restClient.get()
            .uri(uriBuilder -> uriBuilder.path("/api/university-data/generate")
                .queryParam("numberOfCourses", dataRequest.getNumberOfCourses())
                .queryParam("numberOfGroups", dataRequest.getNumberOfGroups())
                .queryParam("numberOfStudents", dataRequest.getNumberOfStudents())
                .queryParam("numberOfTeachers", dataRequest.getNumberOfTeachers())
                .build())
            .retrieve()
            .body(UniversityDataResponse.class);
    }
}
