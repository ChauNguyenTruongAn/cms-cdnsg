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
}