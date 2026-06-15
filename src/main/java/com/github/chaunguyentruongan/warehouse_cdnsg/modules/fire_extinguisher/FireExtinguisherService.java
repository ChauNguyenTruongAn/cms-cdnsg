package com.github.chaunguyentruongan.warehouse_cdnsg.modules.fire_extinguisher;

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
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FireExtinguisherService {

    private final FireExtinguisherRepository repository;
    private final ExtinguisherHistoryRepository historyRepository;
    private final LocationRepository locationRepository;

    @Value("${app.extinguisher.warning-days:15}")
    private int warningDays;

    // ---------------- HELPER METHODS ---------------- //

    private FireExtinguisher getEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bình chữa cháy id: " + id));
    }

    // HÀM MỚI: Tự động tính toán trạng thái dựa trên ngày hết hạn
    private MaintenanceStatus calculateStatus(LocalDate nextRechargeDate) {
        if (nextRechargeDate == null)
            return MaintenanceStatus.OK;

        LocalDate today = LocalDate.now();
        if (nextRechargeDate.isBefore(today) || nextRechargeDate.isEqual(today)) {
            return MaintenanceStatus.EXPIRED;
        } else if (nextRechargeDate.isBefore(today.plusDays(warningDays))) {
            return MaintenanceStatus.WARNING;
        }
        return MaintenanceStatus.OK;
    }

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

    // ALTER TABLE fire_extinguisher ADD COLUMN type_normalized VARCHAR(255);
    public Page<FireExtinguisherResponse> getAll(String keyword, Long zoneId, String type, String weight,
            MaintenanceStatus status,
            Pageable pageable) {
        String safeKeyword = (keyword != null && !keyword.trim().isEmpty()) ? FireExtinguisher.normalize(keyword.trim())
                : null;

        String safeType = (type != null && !type.trim().isEmpty())
                ? FireExtinguisher.normalize(type.trim())
                : null;

        // Gọi thẳng hàm searchWithFilters với mọi tham số
        Page<FireExtinguisher> page = repository.searchWithFilters(safeKeyword, zoneId, safeType, weight, status,
                pageable);

        return page.map(this::mapToResponse);
    }

    public long countByStatus(MaintenanceStatus status) {
        return repository.countByStatus(status);
    }

    public List<ExtinguisherHistory> getHistory(Long id) {
        getEntityById(id);
        return historyRepository.findByExtinguisherIdOrderByRechargeDateDesc(id);
    }

    @Transactional
    public FireExtinguisherResponse create(FireExtinguisherRequest request) {
        log.info("Creating new fire extinguisher: type='{}', weight='{}', quantity={}", 
                request.getType(), request.getWeight(), request.getQuantity());
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

        FireExtinguisher saved = repository.save(fe);
        log.info("Successfully created fire extinguisher ID: {}", saved.getId());
        return mapToResponse(saved);
    }

    @Transactional
    public FireExtinguisherResponse update(Long id, FireExtinguisherRequest request) {
        log.info("Updating fire extinguisher ID: {} -> new type='{}', weight='{}', quantity={}", 
                id, request.getType(), request.getWeight(), request.getQuantity());
        FireExtinguisher fe = getEntityById(id);

        Location location = locationRepository.findById(request.getLocationId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Không tìm thấy Vị trí id: " + request.getLocationId()));

        fe.setLocation(location);
        fe.setType(request.getType());
        fe.setWeight(request.getWeight());
        fe.setQuantity(request.getQuantity());
        fe.setUnit(request.getUnit());
        fe.setNote(request.getNote());

        FireExtinguisher saved = repository.save(fe);
        log.info("Successfully updated fire extinguisher ID: {}", saved.getId());
        return mapToResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Deleting fire extinguisher ID: {}", id);
        FireExtinguisher fe = getEntityById(id);
        List<ExtinguisherHistory> feHis = historyRepository.findByExtinguisherIdOrderByRechargeDateDesc(id);
        log.info("Deleting {} history records associated with fire extinguisher ID: {}", feHis.size(), id);
        feHis.forEach(his -> {
            historyRepository.deleteById(his.getId());
        });
        repository.delete(fe);
        log.info("Successfully deleted fire extinguisher ID: {}", id);
    }

    // SỬA LỖI TẠI ĐÂY: Áp dụng trạng thái tức thời khi nạp bình
    @Transactional
    public FireExtinguisherResponse recharge(Long id, LocalDate rechargeDate, LocalDate nextRechargeDate, String note) {
        FireExtinguisher fe = getEntityById(id);

        LocalDate finalNextDate = (nextRechargeDate != null) ? nextRechargeDate : rechargeDate.plusMonths(6);
        String finalNote = (note != null && !note.trim().isEmpty()) ? note : "Nạp bình bảo dưỡng";

        log.info("Recharging fire extinguisher ID: {}, rechargeDate={}, nextRechargeDate='{}'", 
                id, rechargeDate, finalNextDate);

        fe.setLastRechargeDate(rechargeDate);
        fe.setNextRechargeDate(finalNextDate);

        // Thay thế fe.setStatus(MaintenanceStatus.OK) bằng hàm tự tính
        MaintenanceStatus newStatus = calculateStatus(finalNextDate);
        fe.setStatus(newStatus);

        ExtinguisherHistory history = ExtinguisherHistory.builder()
                .extinguisher(fe)
                .rechargeDate(rechargeDate)
                .nextRechargeDate(finalNextDate)
                .note(finalNote)
                .build();
        historyRepository.save(history);

        FireExtinguisher saved = repository.save(fe);
        log.info("Successfully recharged fire extinguisher ID: {}. New Status: {}", saved.getId(), newStatus);
        return mapToResponse(saved);
    }

    // SỬA LỖI TẠI ĐÂY: Áp dụng trạng thái tức thời khi nạp hàng loạt theo khu vực
    @Transactional
    public void rechargeByZone(Long zoneId, RechargeRequest request) {
        log.info("Recharging all fire extinguishers in Zone ID: {}", zoneId);
        List<FireExtinguisher> extinguishers = repository.findAllByLocationZoneId(zoneId);
        if (extinguishers.isEmpty()) {
            throw new ResourceNotFoundException("Không tìm thấy bình chữa cháy nào trong Khu vực id: " + zoneId);
        }

        LocalDate rechargeDate = request.getRechargeDate();
        LocalDate nextDate = (request.getNextRechargeDate() != null) ? request.getNextRechargeDate()
                : rechargeDate.plusMonths(6);
        String note = (request.getNote() != null && !request.getNote().trim().isEmpty()) ? request.getNote()
                : "Nạp hàng loạt theo khu vực";

        // Tính trước trạng thái để áp dụng cho cả lô
        MaintenanceStatus newStatus = calculateStatus(nextDate);
        log.info("Found {} extinguishers in Zone ID: {}. Target status: {}", extinguishers.size(), zoneId, newStatus);

        for (FireExtinguisher fe : extinguishers) {
            fe.setLastRechargeDate(rechargeDate);
            fe.setNextRechargeDate(nextDate);

            // Thay thế bằng hàm tự tính
            fe.setStatus(newStatus);

            ExtinguisherHistory history = ExtinguisherHistory.builder()
                    .extinguisher(fe)
                    .rechargeDate(rechargeDate)
                    .nextRechargeDate(nextDate)
                    .note(note)
                    .build();
            historyRepository.save(history);
        }

        repository.saveAll(extinguishers);
        log.info("Successfully recharged {} fire extinguishers in Zone ID: {}", extinguishers.size(), zoneId);
    }

    // CẢI TIẾN: Hàm update lịch trình cũng sử dụng lại Helper Method để code gọn
    // gàng hơn
    @Transactional
    public void updateAllStatuses() {
        log.info("Executing batch update of fire extinguisher statuses...");
        List<FireExtinguisher> list = repository.findAll();
        int scanCount = 0;
        int changeCount = 0;
        for (FireExtinguisher fe : list) {
            if (fe.getNextRechargeDate() != null) {
                MaintenanceStatus prevStatus = fe.getStatus();
                MaintenanceStatus currentStatus = calculateStatus(fe.getNextRechargeDate());
                if (prevStatus != currentStatus) {
                    fe.setStatus(currentStatus);
                    changeCount++;
                }
                scanCount++;
            }
        }
        if (changeCount > 0) {
            repository.saveAll(list);
        }
        log.info("Batch update completed. Scanned with next recharge date: {}/{}. Changed statuses count: {}", 
                scanCount, list.size(), changeCount);
    }

    @Scheduled(cron = "0 0 8,15 * * ?") // Cron job tự quét mỗi 8h sáng và 15h hằng ngày
    @Transactional
    public void updateAllStatusesTask() {
        log.info("Cron job triggered: updating fire extinguisher statuses");
        updateAllStatuses();
    }
}