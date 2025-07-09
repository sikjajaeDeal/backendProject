package likelion.beanBa.backendProject.infra.kakaomap.service;

import org.springframework.stereotype.Service;

import likelion.beanBa.backendProject.infra.kakaomap.dto.LocationDTO;
import likelion.beanBa.backendProject.infra.kakaomap.entity.LocationEntity;
import likelion.beanBa.backendProject.infra.kakaomap.repository.LocationRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    // 위치 저장
    public LocationDTO saveLocation(LocationDTO dto) {
        LocationEntity entity = LocationEntity.builder()
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .accuracy(dto.getAccuracy())
                .build();

        LocationEntity saved = locationRepository.save(entity);

        return LocationDTO.builder()
                .latitude(saved.getLatitude())
                .longitude(saved.getLongitude())
                .accuracy(saved.getAccuracy())
                .build();
    }

    // 가장 최근 위치 조회
    public LocationDTO getLatestLocation() {
        LocationEntity entity = locationRepository.findTopByOrderByIdDesc()
                .orElseThrow(() -> new RuntimeException("저장된 위치 정보가 없습니다."));

        return LocationDTO.builder()
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .accuracy(entity.getAccuracy())
                .build();
    }
}
