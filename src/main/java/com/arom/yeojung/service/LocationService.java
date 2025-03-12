package com.arom.yeojung.service;

import com.arom.yeojung.object.Location;
import com.arom.yeojung.object.LocationType;
import com.arom.yeojung.object.dto.LocationRequestDTO;
import com.arom.yeojung.object.dto.LocationResponseDTO;
import com.arom.yeojung.repository.CheckListRepository;
import com.arom.yeojung.repository.LocationRepository;
import com.arom.yeojung.repository.TotalPlanRepository;
import com.arom.yeojung.util.exception.CustomException;
import com.arom.yeojung.util.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationService {
    private final LocationRepository locationRepository;
    // 생성
    @Transactional
    public LocationResponseDTO createLocation(LocationRequestDTO dto) {
        log.info("위치 생성 요청 시작: country: {}, city: {}, district: {}", dto.getCountry(), dto.getCity(), dto.getDistrict());
        Location location = Location.builder()
                .country(dto.getCountry())
                .city(dto.getCity())
                .district(dto.getDistrict())
                .address(dto.getAddress())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .locationType(dto.getLocationType())
                .build();
        Location saved = locationRepository.save(location);
        log.info("위치 생성 성공: id: {}", saved.getLocationId());
        return mapToResponseDTO(saved);
    }

    // 조회 (단건)
    @Transactional
    public LocationResponseDTO getLocation(Long id) {
        log.info("위치 정보 조회 요청: Location id: {}", id);
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("위치 정보 조회 실패: Location id: {}", id);
                    return new CustomException(ErrorCode.LOCATION_NOT_FOUND);
                });
        log.info("위치 정보 조회 성공: Location id: {}", id);
        return mapToResponseDTO(location);
    }

    // 전체 조회
    @Transactional
    public List<LocationResponseDTO> getAllLocations() {
        log.info("전체 위치 조회 요청");
        List<LocationResponseDTO> responseList = locationRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
        log.info("전체 위치 조회 성공: 조회 건수: {}", responseList.size());
        return responseList;
    }

    // LocationType에 따른 조회
    @Transactional
    public List<LocationResponseDTO> getLocationsByType(LocationType locationType){
        log.info("위치 타입별 조회 요청: locationType: {}", locationType);
        List<LocationResponseDTO> responseList = locationRepository.findByLocationType(locationType).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
        log.info("위치 타입별 조회 성공: locationType: {}, 조회 건수: {}", locationType, responseList.size());
        return responseList;
    }

    // 수정
    @Transactional
    public LocationResponseDTO updateLocation(Long id, LocationRequestDTO dto) {
        log.info("위치 정보 수정 요청: id: {}", id);
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("위치 정보 수정 실패: Location id: {}, 위치 정보 조회 실패", id);
                    return new CustomException(ErrorCode.LOCATION_NOT_FOUND);
                });

        location.setCity(dto.getCity());
        location.setDistrict(dto.getDistrict());
        location.setAddress(dto.getAddress());
        location.setLatitude(dto.getLatitude());
        location.setLongitude(dto.getLongitude());
        location.setLocationType(dto.getLocationType());

        Location updated = locationRepository.save(location);
        log.info("위치 수정 성공: Location id: {}", id);
        return mapToResponseDTO(updated);
    }

    // 삭제
    @Transactional
    public void deleteLocation(Long id) {
        log.info("위치 정보 삭제 요청: Location id: {}", id);
        if(!locationRepository.existsById(id)){
            log.error("위치 정보 삭제 실패: Location id: {}, 존재하지 않는 위치", id);
            throw new CustomException(ErrorCode.LOCATION_NOT_FOUND);
        }
        locationRepository.deleteById(id);
        log.info("위치 정보 삭제 성공: Location id: {}", id);
    }

    // Entity -> DTO 매핑
    private LocationResponseDTO mapToResponseDTO(Location location) {
        return LocationResponseDTO.builder()
                .locationId(location.getLocationId())
                .city(location.getCity())
                .district(location.getDistrict())
                .address(location.getAddress())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .locationType(location.getLocationType())
                .build();
    }
}