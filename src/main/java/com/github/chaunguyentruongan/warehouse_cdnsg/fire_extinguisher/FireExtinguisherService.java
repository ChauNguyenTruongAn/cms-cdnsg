package com.github.chaunguyentruongan.warehouse_cdnsg.fire_extinguisher;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.chaunguyentruongan.warehouse_cdnsg.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FireExtinguisherService {

    private final FireExtinguisherRepository repository;
    private final ExtinguisherHistoryRepository historyRepository;
    // BỔ SUNG: Khai báo LocationRepository để xử lý liên kết vị trí
    private final LocationRepository locationRepository;

    // BỔ SUNG: Số ngày cảnh báo sắp hết hạn (Cấu hình trong application.properties,
    // mặc định là 30 ngày)
    @Value("${app.extinguisher.warning-days:30}")
    private int warningDays;

    // ---------------- HELPER METHODS ---------------- //

    // Hàm nội bộ dùng để tìm Entity, tái sử dụng nhiều lần
    private FireExtinguisher getEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bình chữa cháy id: " + id));
    }

    // BỔ SUNG: Ánh xạ Entity sang Response DTO
    private FireExtinguisherResponse mapToResponse(FireExtinguisher fe) {
        return FireExtinguisherResponse.builder()
                .id(fe.getId())
                .locationId(fe.getLocation().getId())
                .locationName(fe.getLocation().getName())
                .zoneId(fe.getLocation().getZone().getId())
                .zoneName(fe.getLocation().getZone().getName())
                .type(fe.getType())
                .weight(fe.getWeight())
                .quantity(fe.getQuantity())
                .unit(fe.getUnit())
                .note(fe.getNote())
                .lastRechargeDate(fe.getLastRechargeDate())
                .nextRechargeDate(fe.getNextRechargeDate())
                .status(fe.getStatus() != null ? fe.getStatus().name() : null)
                .build();
    }

    // ---------------- MAIN LOGIC ---------------- //

    public FireExtinguisherResponse findById(Long id) {
        return mapToResponse(getEntityById(id));
    }

    public List<ZoneExtinguisherStatsResponse> getAdvancedStats() {
        return repository.getAdvancedStats();
    }

    public Page<FireExtinguisherResponse> getAll(String keyword, Pageable pageable) {
        Page<FireExtinguisher> page;
        if (keyword != null && !keyword.trim().isEmpty()) {
            page = repository.search(keyword.trim(), pageable);
        } else {
            page = repository.findAll(pageable);
        }
        // Map Page<Entity> sang Page<Response>
        return page.map(this::mapToResponse);
    }

    public long countByStatus(MaintenanceStatus status) {
        return repository.countByStatus(status);
    }

    public List<ExtinguisherHistory> getHistory(Long id) {
        getEntityById(id); // Kiểm tra tồn tại
        return historyRepository.findByExtinguisherIdOrderByRechargeDateDesc(id);
    }

    @Transactional
    public FireExtinguisherResponse create(FireExtinguisherRequest request) {
        // Tìm Location từ ID
        Location location = locationRepository.findById(request.getLocationId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Không tìm thấy Vị trí id: " + request.getLocationId()));

        FireExtinguisher fe = FireExtinguisher.builder()
                .location(location)
                .type(request.getType())
                .weight(request.getWeight())
                .quantity(request.getQuantity())
                .unit(request.getUnit())
                .note(request.getNote())
                .status(MaintenanceStatus.OK)
                .build();

        return mapToResponse(repository.save(fe));
    }

    @Transactional
    public FireExtinguisherResponse update(Long id, FireExtinguisherRequest request) {
        FireExtinguisher fe = getEntityById(id);

        // Cập nhật Vị trí nếu có thay đổi
        Location location = locationRepository.findById(request.getLocationId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Không tìm thấy Vị trí id: " + request.getLocationId()));

        fe.setLocation(location);
        fe.setType(request.getType());
        fe.setWeight(request.getWeight());
        fe.setQuantity(request.getQuantity());
        fe.setUnit(request.getUnit());
        fe.setNote(request.getNote());

        return mapToResponse(repository.save(fe));
    }

    @Transactional
    public void delete(Long id) {
        FireExtinguisher fe = getEntityById(id);
        repository.delete(fe);
    }

    // CẢI TIẾN: Nạp bình linh hoạt
    @Transactional
    public FireExtinguisherResponse recharge(Long id, LocalDate rechargeDate, LocalDate nextRechargeDate, String note) {
        FireExtinguisher fe = getEntityById(id);

        // Cho phép tùy chỉnh thời gian nạp. Nếu người dùng không nhập hạn, tự cộng thêm
        // 6 tháng
        LocalDate finalNextDate = (nextRechargeDate != null) ? nextRechargeDate : rechargeDate.plusMonths(6);
        String finalNote = (note != null && !note.trim().isEmpty()) ? note : "Nạp bình bảo dưỡng";

        fe.setLastRechargeDate(rechargeDate);
        fe.setNextRechargeDate(finalNextDate);
        fe.setStatus(MaintenanceStatus.OK);

        ExtinguisherHistory history = ExtinguisherHistory.builder()
                .extinguisher(fe)
                .rechargeDate(rechargeDate)
                .nextRechargeDate(finalNextDate)
                .note(finalNote)
                .build();
        historyRepository.save(history);

        return mapToResponse(repository.save(fe));
    }

    // CẢI TIẾN: Sử dụng biến warningDays thay vì fix cứng số 15
    @Transactional
    public void updateAllStatuses() {
        List<FireExtinguisher> list = repository.findAll();
        LocalDate today = LocalDate.now();

        for (FireExtinguisher fe : list) {
            if (fe.getNextRechargeDate() == null)
                continue;

            if (fe.getNextRechargeDate().isBefore(today) || fe.getNextRechargeDate().isEqual(today)) {
                fe.setStatus(MaintenanceStatus.EXPIRED);
            } else if (fe.getNextRechargeDate().isBefore(today.plusDays(warningDays))) {
                fe.setStatus(MaintenanceStatus.WARNING);
            } else {
                fe.setStatus(MaintenanceStatus.OK);
            }
        }
        repository.saveAll(list);
    }

    @Transactional
    public void rechargeByZone(Long zoneId, RechargeRequest request) {
        List<FireExtinguisher> extinguishers = repository.findAllByLocationZoneId(zoneId);
        if (extinguishers.isEmpty()) {
            throw new ResourceNotFoundException("Không tìm thấy bình chữa cháy nào trong Khu vực id: " + zoneId);
        }

        LocalDate rechargeDate = request.getRechargeDate();
        // Nếu không có ngày hết hạn tùy chọn, tự cộng 6 tháng
        LocalDate nextDate = (request.getNextRechargeDate() != null) ? request.getNextRechargeDate()
                : rechargeDate.plusMonths(6);
        String note = (request.getNote() != null && !request.getNote().trim().isEmpty()) ? request.getNote()
                : "Nạp hàng loạt theo khu vực";

        for (FireExtinguisher fe : extinguishers) {
            fe.setLastRechargeDate(rechargeDate);
            fe.setNextRechargeDate(nextDate);
            fe.setStatus(MaintenanceStatus.OK);

            // Lưu lịch sử cho từng bình
            ExtinguisherHistory history = ExtinguisherHistory.builder()
                    .extinguisher(fe)
                    .rechargeDate(rechargeDate)
                    .nextRechargeDate(nextDate)
                    .note(note)
                    .build();
            historyRepository.save(history);
        }

        // Lưu toàn bộ bình đã cập nhật
        repository.saveAll(extinguishers);
    }

    @Scheduled(cron = "0 0 1 * * ?") // Chạy vào 1h sáng mỗi ngày
    @Transactional
    public void updateAllStatusesTask() {
        updateAllStatuses();
    }
}