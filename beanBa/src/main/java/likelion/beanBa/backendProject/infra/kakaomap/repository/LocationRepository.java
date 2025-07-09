package likelion.beanBa.backendProject.infra.kakaomap.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import likelion.beanBa.backendProject.infra.kakaomap.entity.LocationEntity;

public interface LocationRepository extends JpaRepository<LocationEntity, Long> {

    // id 내림차순 정렬 후 첫 번째 데이터 조회
    Optional<LocationEntity> findTopByOrderByIdDesc();
}
