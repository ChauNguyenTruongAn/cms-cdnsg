package com.github.chaunguyentruongan.warehouse_cdnsg.fire_extinguisher;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.github.chaunguyentruongan.warehouse_cdnsg.exception.ResourceNotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FireExtinguisherService {
    private final FireExtinguisherRepository repository;
    private final ExtinguisherHistoryRepository historyRepository;

    public FireExtinguisher findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bình chữa cháy id: " + id));
    }

    public Page<FireExtinguisher> getAll(String keyword, Pageable pageable) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return repository.search(keyword.trim(), pageable);
        }
        return repository.findAll(pageable);
    }

    // Thêm phương thức này để phục vụ thống kê
    public long countByStatus(MaintenanceStatus status) {
        return repository.countByStatus(status);
    }

    public List<ExtinguisherHistory> getHistory(Long id) {
        findById(id);
        return historyRepository.findByExtinguisherIdOrderByRechargeDateDesc(id);
    }

    @Transactional
    public FireExtinguisher create(FireExtinguisherRequest request) {
        FireExtinguisher fe = FireExtinguisher.builder()
                .location(request.getLocation())
                .type(request.getType())
                .weight(request.getWeight())
                .quantity(request.getQuantity())
                .status(MaintenanceStatus.OK)
                .build();
        return repository.save(fe);
    }

    @Transactional
    public FireExtinguisher recharge(Long id, LocalDate rechargeDate) {
        FireExtinguisher fe = findById(id);
        LocalDate nextDate = rechargeDate.plusMonths(6);

        fe.setLastRechargeDate(rechargeDate);
        fe.setNextRechargeDate(nextDate);
        fe.setStatus(MaintenanceStatus.OK);

        ExtinguisherHistory history = ExtinguisherHistory.builder()
                .extinguisher(fe)
                .rechargeDate(rechargeDate)
                .nextRechargeDate(nextDate)
                .note("Nạp định kỳ 6 tháng")
                .build();
        historyRepository.save(history);

        return repository.save(fe);
    }

    @Transactional
    public void updateAllStatuses() {
        List<FireExtinguisher> list = repository.findAll();
        LocalDate today = LocalDate.now();
        for (FireExtinguisher fe : list) {
            if (fe.getNextRechargeDate() == null)
                continue;

            if (fe.getNextRechargeDate().isBefore(today)) {
                fe.setStatus(MaintenanceStatus.EXPIRED);
            } else if (fe.getNextRechargeDate().isBefore(today.plusDays(15))) {
                fe.setStatus(MaintenanceStatus.WARNING);
            } else {
                fe.setStatus(MaintenanceStatus.OK);
            }
        }
        repository.saveAll(list);
    }

    @Transactional
    public FireExtinguisher update(Long id, FireExtinguisherRequest request) {
        FireExtinguisher fe = findById(id);
        fe.setLocation(request.getLocation());
        fe.setType(request.getType());
        fe.setWeight(request.getWeight());
        fe.setQuantity(request.getQuantity());
        return repository.save(fe);
    }

    @Transactional
    public void delete(Long id) {
        FireExtinguisher fe = findById(id);
        repository.delete(fe);
    }

    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional
    public void updateAllStatusesTask() {
        updateAllStatuses();
    }
}