package com.github.chaunguyentruongan.warehouse_cdnsg.projector_maintenance;

import com.github.chaunguyentruongan.warehouse_cdnsg.exception.ResourceNotFoundException;
import com.github.chaunguyentruongan.warehouse_cdnsg.projector_core.Projector;
import com.github.chaunguyentruongan.warehouse_cdnsg.projector_core.ProjectorRepository;
import com.github.chaunguyentruongan.warehouse_cdnsg.projector_core.ProjectorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectorMaintenanceService {

    private final ProjectorMaintenanceRepository projectorMaintenanceRepository;
    private final MaintenanceTicketRepository ticketRepository;
    private final ProjectorRepository projectorRepository;

    @Transactional
    public MaintenanceTicket createTicket(MaintenanceTicketRequest request) {
        // 1. Tạo thông tin chung của Phiếu
        MaintenanceTicket ticket = MaintenanceTicket.builder()
                .ticketCode(request.getTicketCode() != null ? request.getTicketCode()
                        : "BT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .startDate(request.getStartDate() != null ? request.getStartDate() : LocalDate.now())
                .technician(request.getTechnician())
                .generalNote(request.getGeneralNote())
                .details(new ArrayList<>())
                .build();

        // 2. Xử lý danh sách máy chiếu kèm theo
        for (MaintenanceDetailRequest detailReq : request.getDetails()) {
            Projector projector = projectorRepository.findByIdWithLock(detailReq.getProjectorId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Không tìm thấy máy chiếu ID: " + detailReq.getProjectorId()));

            if (projector.getStatus() == ProjectorStatus.BORROWED || projector.getStatus() == ProjectorStatus.IN_USE) {
                throw new IllegalArgumentException(
                        "Máy chiếu " + projector.getName() + " đang được sử dụng/mượn, không thể mang đi bảo trì!");
            }

            projector.setStatus(ProjectorStatus.UNDER_MAINTENANCE);
            projectorRepository.save(projector);

            // SỬA LỖI TÊN CLASS TẠI ĐÂY (Thay ProjectorMaintenanceDetail thành
            // ProjectorMaintenance)
            ProjectorMaintenanceDetail detail = ProjectorMaintenanceDetail.builder()
                    .ticket(ticket)
                    .projector(projector)
                    .description(detailReq.getDescription())
                    .cost(detailReq.getCost())
                    .build();

            ticket.getDetails().add(detail);
        }

        return ticketRepository.save(ticket);
    }

    @Transactional
    public MaintenanceTicket completeTicket(Long ticketId, LocalDate completionDate) {
        MaintenanceTicket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu bảo trì!"));

        if (ticket.getCompletionDate() != null) {
            throw new IllegalArgumentException("Phiếu bảo trì này đã được hoàn tất trước đó!");
        }

        // 1. Cập nhật ngày hoàn tất
        ticket.setCompletionDate(completionDate != null ? completionDate : LocalDate.now());

        // 2. Nhả trạng thái cho tất cả máy chiếu trong phiếu về AVAILABLE
        for (ProjectorMaintenanceDetail detail : ticket.getDetails()) {
            Projector projector = detail.getProjector();
            projector.setStatus(ProjectorStatus.AVAILABLE);
            projectorRepository.save(projector);
        }

        return ticketRepository.save(ticket);
    }

    public List<MaintenanceTicket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public List<ProjectorMaintenanceDetail> getHistoryByProjectorId(Long projectorId) {
        // Lưu ý: Đảm bảo bạn đã inject ProjectorMaintenanceRepository vào đầu Service
        // nhé
        return projectorMaintenanceRepository.findHistoryByProjectorId(projectorId);
    }

    @Transactional
    public MaintenanceTicket completeTicket(Long ticketId, CompleteMaintenanceRequest request) {
        MaintenanceTicket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu bảo trì!"));

        if (ticket.getCompletionDate() != null) {
            throw new IllegalArgumentException("Phiếu bảo trì này đã được hoàn tất trước đó!");
        }

        // 1. Cập nhật ngày hoàn tất
        ticket.setCompletionDate(request.getCompletionDate() != null ? request.getCompletionDate() : LocalDate.now());

        // 2. Cập nhật trạng thái từng máy chiếu dựa theo dữ liệu Frontend gửi lên
        for (ProjectorMaintenanceDetail detail : ticket.getDetails()) {
            Projector projector = detail.getProjector();
            ProjectorStatus newStatus = ProjectorStatus.AVAILABLE; // Mặc định nếu không có thông tin

            if (request.getResults() != null) {
                for (CompleteMaintenanceRequest.MaintenanceResult result : request.getResults()) {
                    if (result.getProjectorId().equals(projector.getId()) && result.getStatus() != null) {
                        newStatus = result.getStatus();
                        break;
                    }
                }
            }

            projector.setStatus(newStatus);
            projectorRepository.save(projector);
        }

        return ticketRepository.save(ticket);
    }

    // ... CÁC HÀM CŨ GIỮ NGUYÊN ...

    // THÊM MỚI: Cập nhật phiếu bảo trì đang diễn ra
    @Transactional
    public MaintenanceTicket updateTicket(Long ticketId, MaintenanceTicketRequest request) {
        MaintenanceTicket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu bảo trì!"));

        if (ticket.getCompletionDate() != null) {
            throw new IllegalArgumentException("Không thể sửa phiếu bảo trì đã hoàn tất!");
        }

        // Cập nhật thông tin chung
        ticket.setTicketCode(request.getTicketCode());
        if (request.getStartDate() != null)
            ticket.setStartDate(request.getStartDate());
        ticket.setTechnician(request.getTechnician());
        ticket.setGeneralNote(request.getGeneralNote());

        List<ProjectorMaintenanceDetail> currentDetails = ticket.getDetails();
        List<MaintenanceDetailRequest> requestedDetails = request.getDetails();

        // 1. Gỡ bỏ những máy chiếu không còn trong danh sách request (bị uncheck)
        List<ProjectorMaintenanceDetail> toRemove = new ArrayList<>();
        for (ProjectorMaintenanceDetail existingDetail : currentDetails) {
            boolean isKept = requestedDetails.stream()
                    .anyMatch(req -> req.getProjectorId().equals(existingDetail.getProjector().getId()));
            if (!isKept) {
                Projector projector = existingDetail.getProjector();
                projector.setStatus(ProjectorStatus.AVAILABLE); // Trả lại trạng thái rảnh
                projectorRepository.save(projector);
                toRemove.add(existingDetail);
            }
        }
        currentDetails.removeAll(toRemove);

        // 2. Cập nhật máy cũ hoặc Thêm máy mới vào phiếu
        for (MaintenanceDetailRequest reqDetail : requestedDetails) {
            ProjectorMaintenanceDetail existingDetail = currentDetails.stream()
                    .filter(d -> d.getProjector().getId().equals(reqDetail.getProjectorId()))
                    .findFirst()
                    .orElse(null);

            if (existingDetail != null) {
                // Đã có trong phiếu -> Cập nhật mô tả, chi phí
                existingDetail.setDescription(reqDetail.getDescription());
                existingDetail.setCost(reqDetail.getCost());
            } else {
                // Máy mới được thêm vào phiếu
                Projector projector = projectorRepository.findByIdWithLock(reqDetail.getProjectorId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Không tìm thấy máy chiếu ID: " + reqDetail.getProjectorId()));

                if (projector.getStatus() == ProjectorStatus.BORROWED
                        || projector.getStatus() == ProjectorStatus.IN_USE) {
                    throw new IllegalArgumentException("Máy chiếu " + projector.getName() + " đang bận!");
                }

                projector.setStatus(ProjectorStatus.UNDER_MAINTENANCE);
                projectorRepository.save(projector);

                ProjectorMaintenanceDetail newDetail = ProjectorMaintenanceDetail.builder()
                        .ticket(ticket)
                        .projector(projector)
                        .description(reqDetail.getDescription())
                        .cost(reqDetail.getCost())
                        .build();
                currentDetails.add(newDetail);
            }
        }

        return ticketRepository.save(ticket);
    }
}