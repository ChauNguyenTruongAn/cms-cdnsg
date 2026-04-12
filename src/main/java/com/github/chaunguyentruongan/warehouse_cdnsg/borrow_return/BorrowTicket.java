package com.github.chaunguyentruongan.warehouse_cdnsg.borrow_return;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "borrow_ticket")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String borrowCode;

    @Column(unique = true)
    private String returnCode;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "borrow_ticket_item", joinColumns = @JoinColumn(name = "ticket_id"))
    @Builder.Default
    private List<BorrowTicketItem> items = new ArrayList<>();
    
    private String borrowerName;
    private String department;
    private String email;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime borrowTime;
    private LocalDateTime returnTime;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Column(nullable = false)
    @Builder.Default
    private boolean deleted = false;
}