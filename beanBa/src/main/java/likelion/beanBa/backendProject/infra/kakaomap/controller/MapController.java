package likelion.beanBa.backendProject.infra.kakaomap.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import likelion.beanBa.backendProject.infra.kakaomap.dto.LocationDTO;
import likelion.beanBa.backendProject.infra.kakaomap.service.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MapController {

    private final LocationService locationService;

    @GetMapping("/radius_map")
    public String  radiusMap() {
        return "map_click_location.html";
    }


    @GetMapping("/geo_map")
    public String geoMap(){
        return "geo_map.html";
    }

    // 클라이언트로부터 위치 정보 저장
    @PostMapping("/location")
    @ResponseBody
    public ResponseEntity<Void> saveLocation(@RequestBody LocationDTO locationDTO) {
        locationService.saveLocation(locationDTO);
        log.info("위치 저장 완료 → lat={}, lng={}, acc={}",
                locationDTO.getLatitude(), locationDTO.getLongitude(), locationDTO.getAccuracy());
        return ResponseEntity.ok().build();
    }

    // 가장 최근에 저장된 위치 정보 반환
    @GetMapping("/location/latest")
    @ResponseBody
    public ResponseEntity<LocationDTO> getLatestLocation() {
        LocationDTO latestLocation = locationService.getLatestLocation();
        log.info("가장 최근 위치 정보 반환 → lat={}, lng={}, acc={}",
                latestLocation.getLatitude(), latestLocation.getLongitude(), latestLocation.getAccuracy());
        return ResponseEntity.ok(latestLocation);
    }
}
